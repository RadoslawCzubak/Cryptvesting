package com.rczubak.cryptvesting.data.repository

import com.rczubak.cryptvesting.domain.model.Wallet
import com.rczubak.cryptvesting.common.Resource
import com.rczubak.cryptvesting.common.TransactionCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val transactionsRepository: TransactionsRepository,
    private val nomicsRepository: NomicsRepository
) {
    private val _profit: MutableSharedFlow<Resource<Double>> = MutableSharedFlow(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val profit: SharedFlow<Resource<Double>> = _profit

    suspend fun getCurrentProfit(refresh: Boolean = true) =
        withContext(Dispatchers.IO) {
            if (refresh) {
                val ownedCrypto = transactionsRepository.getOwnedCrypto().data!!
                nomicsRepository.getCryptoCurrenciesState(ownedCrypto, refresh)
            }
            val transactionsResource = transactionsRepository.getAllTransactions()
            val ownedCryptoResource = transactionsRepository.getOwnedCrypto()
            if (!transactionsResource.isSuccess() && !ownedCryptoResource.isSuccess() && ownedCryptoResource.data!!.isEmpty()) {
                _profit.tryEmit(Resource.error("Error in transactions"))
                return@withContext
            }
            val pricesResource =
                nomicsRepository.getCryptoCurrenciesState(ownedCryptoResource.data!!)
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

    suspend fun getWalletWithValue() =
        withContext(Dispatchers.IO) {
            val walletCoinsRepository = transactionsRepository.getWalletCoins()
            if (!walletCoinsRepository.isSuccess())
                return@withContext Resource.error("Prices error")
            val walletCoins = walletCoinsRepository.data!!
            val prices =
                nomicsRepository.getCryptoCurrenciesState(walletCoins.map { it.currencySymbol })
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
}

