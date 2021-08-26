package com.rczubak.cryptvesting.data.dao.transactions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rczubak.cryptvesting.data.models.entities.TransactionEntity

@Dao
interface TransactionsDao {
    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllTransactions(transactions: List<TransactionEntity>)
}