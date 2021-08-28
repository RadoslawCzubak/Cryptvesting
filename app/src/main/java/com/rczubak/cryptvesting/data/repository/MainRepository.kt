package com.rczubak.cryptvesting.data.repository

import com.rczubak.cryptvesting.data.network.services.Resource
import com.rczubak.cryptvesting.utils.TransactionCalculator
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

    suspend fun getCurrentProfit() =
        withContext(Dispatchers.IO) {
            val transactionsResource = transactionsRepository.getAllTransactions()
            val ownedCryptoResource = transactionsRepository.getOwnedCrypto()
            if (!transactionsResource.isSuccess() && !ownedCryptoResource.isSuccess()) {
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
}
