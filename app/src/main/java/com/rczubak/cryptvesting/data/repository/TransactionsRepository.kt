package com.rczubak.cryptvesting.data.repository

import com.rczubak.cryptvesting.data.dao.transactions.TransactionsDao
import com.rczubak.cryptvesting.data.models.domain.TransactionModel
import com.rczubak.cryptvesting.data.models.entities.TransactionEntity
import com.rczubak.cryptvesting.data.network.services.Resource
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
}