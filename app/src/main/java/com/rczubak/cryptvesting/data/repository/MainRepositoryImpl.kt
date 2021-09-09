package com.rczubak.cryptvesting.data.repository

import com.rczubak.cryptvesting.common.Error
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
import com.rczubak.cryptvesting.domain.model.Wallet
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
    private val nomicsApi: NomicsApi,
    private val cryptocurrenciesDao: CryptocurrenciesDao,
    private val transactionsDao: TransactionsDao
) : MainRepository {
    private val _profit: MutableSharedFlow<Resource<Double>> = MutableSharedFlow(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val profit: SharedFlow<Resource<Double>> = _profit

    override suspend fun getCurrentProfit(refresh: Boolean): Resource<Double> {
        val ownedCryptoSymbols = transactionsDao.getOwnedCrypto()
        if (ownedCryptoSymbols.isEmpty()) {
            _profit.tryEmit(Resource.error(code = Error.NO_CRYPTO_OWNED.code))
            return Resource.error(code = Error.NO_CRYPTO_OWNED.code)
        }
        if (refresh) {
            val response = nomicsApi.getCryptocurrenciesCurrentInfo(
                prepareCommaSeparatedQueryParameters(
                    ownedCryptoSymbols
                )
            )
            if (response.isSuccessful)
                cryptocurrenciesDao.insertAll(response.body()!!.map { CryptocurrencyEntity(it) })
            else {
                _profit.tryEmit(Resource.error(code = Error.INTERNET_ERROR.code))
                return Resource.error(code = Error.INTERNET_ERROR.code)
            }
        }
        val prices = cryptocurrenciesDao.getCryptoCurrenciesStateBySymbol(ownedCryptoSymbols).map {
            CryptoCurrencyModel(it)
        }
        val transactions = transactionsDao.getAllTransactions().map { TransactionModel(it) }
        var currentProfit: Double
        try {
            currentProfit = TransactionCalculator.calculateProfitInUSD(
                ArrayList(transactions), ArrayList(prices)
            )
        } catch (e: IllegalArgumentException) {
            _profit.tryEmit(Resource.error(code = Error.NO_PRICES_FOR_ALL_OWNED_CRYPTO.code))
            return Resource.error(code = Error.NO_PRICES_FOR_ALL_OWNED_CRYPTO.code)
        }
        _profit.tryEmit(Resource.success(currentProfit))
        return Resource.success(currentProfit)
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
            var value: Double
            try {
                value =
                    TransactionCalculator.calculateCryptoValue(
                        walletCoins.map { it.currencySymbol to it.amount }
                            .toMap(), ArrayList(prices.data!!)
                    )
            } catch (e: IllegalArgumentException) {
                return@withContext Resource.error("")
            }
            val walletCoinsWithUpdatedValues =
                TransactionCalculator.calculateWalletCoinValues(walletCoins, prices.data!!)
            Resource.success(Wallet(walletCoinsWithUpdatedValues, value))
        }

    private suspend fun getCryptoCurrenciesState(
        cryptoSymbols: List<String>): Resource<List<CryptoCurrencyModel>> {
        var cryptoStates = cryptocurrenciesDao.getCryptoCurrenciesStateBySymbol(cryptoSymbols)
        return Resource.success(cryptoStates.map { CryptoCurrencyModel(it) })
    }

    override suspend fun addAllTransactions(transactions: ArrayList<TransactionModel>): Resource<Nothing> {
        transactionsDao.addAllTransactions(transactions.map { TransactionEntity(it) })
        return Resource.success()
    }

    private suspend fun getWalletCoins(): Resource<List<WalletCoin>> {
        val transactions = transactionsDao.getAllTransactions()
        val walletCoins =
            TransactionCalculator.calculateWallet(ArrayList(transactions.map { TransactionModel(it) }))
        return Resource.success(walletCoins)
    }
}

