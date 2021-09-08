package com.rczubak.cryptvesting.data.repository

import com.rczubak.cryptvesting.data.database.transactions.TransactionsDao
import com.rczubak.cryptvesting.domain.model.TransactionModel
import com.rczubak.cryptvesting.domain.model.WalletCoin
import com.rczubak.cryptvesting.data.database.entities.TransactionEntity
import com.rczubak.cryptvesting.utils.Resource
import com.rczubak.cryptvesting.utils.TransactionCalculator
import javax.inject.Inject

class TransactionsRepository @Inject constructor(
    private val transactionsDao: TransactionsDao
) {
    suspend fun getAllTransactions(): Resource<ArrayList<TransactionModel>> {
        return Resource.success(
            ArrayList(
                transactionsDao.getAllTransactions().map { TransactionModel(it) })
        )
    }

    suspend fun addAllTransactions(transactions: ArrayList<TransactionModel>): Resource<Nothing> {
        transactionsDao.addAllTransactions(transactions.map { TransactionEntity(it) })
        return Resource.success()
    }

    suspend fun getOwnedCrypto(): Resource<List<String>> {
        return Resource.success(transactionsDao.getOwnedCrypto())
    }

    suspend fun getWalletCoins(): Resource<List<WalletCoin>> {
        val transactions = transactionsDao.getAllTransactions()
        val walletCoins =
            TransactionCalculator.calculateWallet(ArrayList(transactions.map { TransactionModel(it) }))
        return Resource.success(walletCoins)
    }
}