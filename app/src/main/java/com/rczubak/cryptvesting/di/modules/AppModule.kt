package com.rczubak.cryptvesting.di.modules

import android.content.Context
import androidx.room.Room
import com.rczubak.cryptvesting.data.dao.transactions.TransactionsDao
import com.rczubak.cryptvesting.data.dao.transactions.TransactionsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
final class AppModule {
    @Provides
    fun provideTransactionsDao(@ApplicationContext applicationContext: Context): TransactionsDao {
        val db = Room.databaseBuilder(
            applicationContext,
            TransactionsDatabase::class.java, "database-name"
        ).build()

        return db.transactionDao()
    }
}