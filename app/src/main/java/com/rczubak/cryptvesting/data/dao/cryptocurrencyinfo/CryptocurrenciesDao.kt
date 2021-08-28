package com.rczubak.cryptvesting.data.dao.cryptocurrencyinfo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rczubak.cryptvesting.data.models.domain.CryptoCurrencyModel
import com.rczubak.cryptvesting.data.models.entities.CryptocurrencyEntity

@Dao
interface CryptocurrenciesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cryptocurrencies: List<CryptocurrencyEntity>)

    @Query("SELECT * FROM cryptocurrencies")
    suspend fun getAllCryptocurrenciesState(): List<CryptocurrencyEntity>

    @Query("SELECT * FROM cryptocurrencies WHERE symbol = :symbol")
    suspend fun getCryptocurrencyStateBySymbol(symbol: String): CryptocurrencyEntity

    @Query("SELECT * FROM cryptocurrencies WHERE symbol IN (:symbols)")
    suspend fun getCryptoCurrenciesStateBySymbol(symbols: List<String>): List<CryptocurrencyEntity>
}