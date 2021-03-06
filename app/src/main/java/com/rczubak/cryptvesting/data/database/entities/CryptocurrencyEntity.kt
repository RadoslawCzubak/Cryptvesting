package com.rczubak.cryptvesting.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rczubak.cryptvesting.common.roomUtils.Converters
import com.rczubak.cryptvesting.domain.model.CryptoCurrencyModel
import org.threeten.bp.LocalDateTime

@Entity(tableName = "cryptocurrencies")
@TypeConverters(Converters::class)
data class CryptocurrencyEntity(
    val name: String,
    val symbol: String,
    val price: Double,
    val priceDate: LocalDateTime?,
    val priceCurrency: String,
    val logoUrl: String
) {
    @PrimaryKey
    var id: Int = (name + symbol).hashCode()

    constructor(crypto: CryptoCurrencyModel) : this(
        crypto.name,
        crypto.symbol,
        crypto.price,
        crypto.priceDate,
        crypto.priceCurrency,
        crypto.logoUrl
    )
}