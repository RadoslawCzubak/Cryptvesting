package com.rczubak.cryptvesting.data.models.domain

import com.rczubak.cryptvesting.data.models.entities.CryptocurrencyEntity
import org.threeten.bp.LocalDateTime

data class CryptoCurrencyModel(
    val name: String,
    val symbol: String,
    val price: Double,
    val priceDate: LocalDateTime,
    val priceCurrency: String
) {
    constructor(crypto: CryptocurrencyEntity) : this(
        crypto.name,
        crypto.symbol,
        crypto.price,
        crypto.priceDate,
        crypto.priceCurrency
    )
}