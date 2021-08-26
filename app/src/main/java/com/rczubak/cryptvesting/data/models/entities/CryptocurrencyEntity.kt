package com.rczubak.cryptvesting.data.models.entities

import androidx.room.Entity
import com.rczubak.cryptvesting.data.models.domain.CryptoCurrencyModel
import org.threeten.bp.LocalDateTime

@Entity(tableName = "Cryptocurrency")
data class CryptocurrencyEntity(
    val name: String,
    val symbol: String,
    val price: Double,
    val priceDate: LocalDateTime,
    val priceCurrency: String
) {
    var id: Int = (name + symbol).hashCode()

    constructor(crypto: CryptoCurrencyModel) : this(
        crypto.name,
        crypto.symbol,
        crypto.price,
        crypto.priceDate,
        crypto.priceCurrency
    )
}