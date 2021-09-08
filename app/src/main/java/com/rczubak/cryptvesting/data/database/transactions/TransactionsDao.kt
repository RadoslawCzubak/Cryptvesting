package com.rczubak.cryptvesting.data.database.transactions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rczubak.cryptvesting.data.database.entities.TransactionEntity

@Dao
interface TransactionsDao {
    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllTransactions(transactions: List<TransactionEntity>)

    @Query("SELECT DISTINCT buyCoin FROM transactions")
    suspend fun getOwnedCrypto(): List<String>
}