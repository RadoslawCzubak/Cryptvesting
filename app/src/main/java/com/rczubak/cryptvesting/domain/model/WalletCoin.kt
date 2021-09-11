package com.rczubak.cryptvesting.domain.model

data class WalletCoin(
    val amount: Double,
    val currencySymbol: String,
    val valueInUSD: Double? = null,
    val logoUrl: String? = null
)