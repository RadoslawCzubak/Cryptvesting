package com.rczubak.cryptvesting.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rczubak.cryptvesting.data.models.domain.CryptoCurrencyModel
import com.rczubak.cryptvesting.utils.roomUtils.Converters
import org.threeten.bp.LocalDateTime

@Entity(tableName = "cryptocurrencies")
@TypeConverters(Converters::class)
data class CryptocurrencyEntity(
    val name: String,
    val symbol: String,
    val price: Double,
    val priceDate: LocalDateTime?,
    val priceCurrency: String
) {
    @PrimaryKey var id: Int = (name + symbol).hashCode()

    constructor(crypto: CryptoCurrencyModel) : this(
        crypto.name,
        crypto.symbol,
        crypto.price,
        crypto.priceDate,
        crypto.priceCurrency
    )
}