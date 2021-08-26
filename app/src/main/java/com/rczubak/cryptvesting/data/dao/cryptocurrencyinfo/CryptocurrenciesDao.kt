package com.rczubak.cryptvesting.data.dao.cryptocurrencyinfo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rczubak.cryptvesting.data.models.entities.CryptocurrencyEntity

@Dao
interface CryptocurrenciesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cryptocurrencies: ArrayList<CryptocurrencyEntity>)

    @Query("SELECT * FROM cryptocurrencies")
    fun getAllCryptocurrenciesState(): List<CryptocurrencyEntity>

    @Query("SELECT * FROM cryptocurrencies WHERE symbol = :symbol")
    fun getCryptocurrencyStateBySymbol(symbol: String): CryptocurrencyEntity
}