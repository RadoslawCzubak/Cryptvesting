package com.rczubak.cryptvesting.domain.model

import com.rczubak.cryptvesting.data.database.entities.CryptocurrencyEntity
import com.squareup.moshi.Json
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException


data class CryptoCurrencyModel(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "currency") val symbol: String,
    @field:Json(name = "price") val price: Double,
    @field:Json(name = "price_date") val price_date: String?,
    val _priceCurrency: String?,
    @field:Json(name = "logo_url") val _logoUrl: String? = ""
) {
    val priceCurrency
        get() = _priceCurrency ?: "USD"
    val logoUrl
        get() = _logoUrl ?: ""
    val priceDate: LocalDateTime?
        get() {
            return try {
                if (price_date != null) {
                    LocalDateTime.parse(price_date, DateTimeFormatter.ISO_DATE_TIME)
                } else {
                    null
                }
            } catch (e: DateTimeParseException) {
                null
            }
        }

    constructor(crypto: CryptocurrencyEntity) : this(
        crypto.name,
        crypto.symbol,
        crypto.price,
        crypto.priceDate?.format(DateTimeFormatter.ISO_DATE_TIME) ?: "",
        crypto.priceCurrency,
        crypto.logoUrl ?: ""
    )

    constructor(
        name: String,
        symbol: String,
        price: Double,
        priceDate: LocalDateTime,
        priceCurrency: String = "USD"
    ) : this(
        name,
        symbol,
        price,
        priceDate.format(DateTimeFormatter.ISO_DATE_TIME),
        priceCurrency
    )
}