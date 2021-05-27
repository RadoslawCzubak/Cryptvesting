package com.rczubak.cryptvesting.data.models.domain

import org.threeten.bp.LocalDateTime

data class CryptoCurrencyModel(
    val name: String,
    val symbol: String,
    val price: Double,
    val priceDate: LocalDateTime,
    val priceCurrency: String
)