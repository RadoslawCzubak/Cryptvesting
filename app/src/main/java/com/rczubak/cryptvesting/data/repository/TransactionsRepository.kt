package com.rczubak.cryptvesting.data.repository

import com.rczubak.cryptvesting.data.dao.transactions.TransactionsDao
import com.rczubak.cryptvesting.data.models.domain.TransactionModel
import com.rczubak.cryptvesting.data.models.entities.TransactionEntity
import javax.inject.Inject

class TransactionsRepository @Inject constructor(
    private val transactionsDao: TransactionsDao
) {
    suspend fun getAllTransactions(): ArrayList<TransactionModel> {
        return ArrayList(
            transactionsDao.getAllTransactions().map { TransactionModel(it) })
    }

    suspend fun addAllTransactions(transactions: ArrayList<TransactionModel>) {
        transactionsDao.addAllTransactions(transactions.map { TransactionEntity(it) })
    }
}