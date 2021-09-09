package com.rczubak.cryptvesting.domain.repository

import com.rczubak.cryptvesting.common.Resource
import com.rczubak.cryptvesting.domain.model.CryptoCurrencyModel
import com.rczubak.cryptvesting.domain.model.TransactionModel
import com.rczubak.cryptvesting.domain.model.Wallet
import com.rczubak.cryptvesting.domain.model.WalletCoin
import kotlinx.coroutines.flow.SharedFlow

interface MainRepository {

    val profit: SharedFlow<Resource<Double>>

    suspend fun getCurrentProfit(refresh: Boolean = true): Resource<Double>

    suspend fun getWalletWithValue(): Resource<Wallet>

    suspend fun syncStatesOfAllOwnedCrypto(ownedCryptoSymbols: List<String>)

    suspend fun getCryptoCurrenciesState(
        cryptoSymbols: List<String>,
        refresh: Boolean = false
    ): Resource<List<CryptoCurrencyModel>>

    suspend fun getAllTransactions(): Resource<ArrayList<TransactionModel>>

    suspend fun addAllTransactions(transactions: ArrayList<TransactionModel>): Resource<Nothing>

    suspend fun getOwnedCrypto(): Resource<List<String>>

    suspend fun getWalletCoins(): Resource<List<WalletCoin>>
}