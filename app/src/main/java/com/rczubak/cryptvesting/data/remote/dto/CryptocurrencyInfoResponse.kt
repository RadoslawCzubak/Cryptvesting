package com.rczubak.cryptvesting.data.remote.dto

import com.squareup.moshi.Json

data class CryptocurrencyInfoResponse(
    @Json(name = "currency") val currency: String,
    @Json(name = "id")val id:String,
    @Json(name = "status")val status: String,
    @Json(name = "price")val price: Double,
    @Json(name = "price_date")val priceDate: String,
    @Json(name = "price_timestamp")val priceTimestamp: String,
    @Json(name = "symbol")val symbol: String,
    @Json(name = "logo_url")val logoUrl: String,
)