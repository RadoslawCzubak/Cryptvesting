package com.rczubak.cryptvesting.data.dao.transactions

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rczubak.cryptvesting.data.models.entities.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1)
abstract class TransactionsDatabase: RoomDatabase() {
    abstract fun transactionDao(): TransactionsDao
}