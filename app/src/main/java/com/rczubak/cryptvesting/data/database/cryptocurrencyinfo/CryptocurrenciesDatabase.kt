package com.rczubak.cryptvesting.data.database.cryptocurrencyinfo

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rczubak.cryptvesting.data.database.entities.CryptocurrencyEntity

@Database(entities = [CryptocurrencyEntity::class], version = 1)
abstract class CryptocurrenciesDatabase: RoomDatabase() {
    abstract fun dao(): CryptocurrenciesDao
}