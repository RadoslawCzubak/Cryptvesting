package com.rczubak.cryptvesting.data.repository

import com.rczubak.cryptvesting.domain.model.Wallet
import com.rczubak.cryptvesting.common.Resource
import com.rczubak.cryptvesting.common.TransactionCalculator
import com.rczubak.cryptvesting.common.prepareCommaSeparatedQueryParameters
import com.rczubak.cryptvesting.data.database.cryptocurrencyinfo.CryptocurrenciesDao
import com.rczubak.cryptvesting.data.database.entities.CryptocurrencyEntity
import com.rczubak.cryptvesting.data.database.entities.TransactionEntity
import com.rczubak.cryptvesting.data.database.transactions.TransactionsDao
import com.rczubak.cryptvesting.data.remote.services.NomicsApi
import com.rczubak.cryptvesting.domain.model.CryptoCurrencyModel
import com.rczubak.cryptvesting.domain.model.TransactionModel
import com.rczubak.cryptvesting.domain.model.WalletCoin
import com.rczubak.cryptvesting.domain.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


class MainRepositoryImpl @Inject constructor(
    private val api: NomicsApi,
    private val cryptocurrenciesDao: CryptocurrenciesDao,
    private val transactionsDao: TransactionsDao
): MainRepository {
    private val _profit: MutableSharedFlow<Resource<Double>> = MutableSharedFlow(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val profit: SharedFlow<Resource<Double>> = _profit

    override suspend fun getCurrentProfit(refresh: Boolean) =
        withContext(Dispatchers.IO) {
            if (refresh) {
                val ownedCrypto = getOwnedCrypto().data!!
                getCryptoCurrenciesState(ownedCrypto, refresh)
            }
            val transactionsResource = getAllTransactions()
            val ownedCryptoResource = getOwnedCrypto()
            if (!transactionsResource.isSuccess() && !ownedCryptoResource.isSuccess() && ownedCryptoResource.data!!.isEmpty()) {
                _profit.tryEmit(Resource.error("Error in transactions"))
                return@withContext
            }
            val pricesResource =
                getCryptoCurrenciesState(ownedCryptoResource.data!!)
            if (!pricesResource.isSuccess()) {
                _profit.tryEmit(Resource.error("Prices error."))
                return@withContext
            }
            val profit = TransactionCalculator.calculateProfitInUSD(
                transactionsResource.data!!,
                ArrayList(pricesResource.data!!)
            )
            _profit.tryEmit(Resource.success(profit))
        }

    override suspend fun getWalletWithValue() =
        withContext(Dispatchers.IO) {
            val walletCoinsRepository = getWalletCoins()
            if (!walletCoinsRepository.isSuccess())
                return@withContext Resource.error("Prices error")
            val walletCoins = walletCoinsRepository.data!!
            val prices =
                getCryptoCurrenciesState(walletCoins.map { it.currencySymbol })
            if (!prices.isSuccess())
                return@withContext Resource.error("Prices error")
            val value =
                TransactionCalculator.calculateCryptoValue(
                    walletCoins.map { it.currencySymbol to it.amount }
                        .toMap(), ArrayList(prices.data!!)
                )
            val walletCoinsWithUpdatedValues =
                TransactionCalculator.calculateWalletCoinValues(walletCoins, prices.data!!)
            Resource.success(Wallet(walletCoinsWithUpdatedValues, value))
        }

    override suspend fun syncStatesOfAllOwnedCrypto(ownedCryptoSymbols: List<String>) {
        val idsQueryParam = prepareCommaSeparatedQueryParameters(ownedCryptoSymbols)
        val response = api.getCryptocurrenciesCurrentInfo(idsQueryParam)
        if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
            val cryptocurrencies = response.body()
            cryptocurrenciesDao.insertAll(cryptocurrencies!!.map {
                CryptocurrencyEntity(it)
            })
        }
        Timber.d(cryptocurrenciesDao.getAllCryptocurrenciesState().toString())
    }

    override suspend fun getCryptoCurrenciesState(
        cryptoSymbols: List<String>,
        refresh: Boolean
    ): Resource<List<CryptoCurrencyModel>> {
        if (refresh) {
            syncStatesOfAllOwnedCrypto(cryptoSymbols)
        }
        var cryptoStates = cryptocurrenciesDao.getCryptoCurrenciesStateBySymbol(cryptoSymbols)
        if (cryptoStates.isEmpty()) {
            syncStatesOfAllOwnedCrypto(cryptoSymbols)
            cryptoStates = cryptocurrenciesDao.getCryptoCurrenciesStateBySymbol(cryptoSymbols)
        }
        return Resource.success(cryptoStates.map { CryptoCurrencyModel(it) })
    }

    override suspend fun getAllTransactions(): Resource<ArrayList<TransactionModel>> {
        return Resource.success(
            ArrayList(
                transactionsDao.getAllTransactions().map { TransactionModel(it) })
        )
    }

    override suspend fun addAllTransactions(transactions: ArrayList<TransactionModel>): Resource<Nothing> {
        transactionsDao.addAllTransactions(transactions.map { TransactionEntity(it) })
        return Resource.success()
    }

    override suspend fun getOwnedCrypto(): Resource<List<String>> {
        return Resource.success(transactionsDao.getOwnedCrypto())
    }

    override suspend fun getWalletCoins(): Resource<List<WalletCoin>> {
        val transactions = transactionsDao.getAllTransactions()
        val walletCoins =
            TransactionCalculator.calculateWallet(ArrayList(transactions.map { TransactionModel(it) }))
        return Resource.success(walletCoins)
    }
}

