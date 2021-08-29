package com.rczubak.cryptvesting.data.models.domain

data class Wallet (
    val walletCoins: List<WalletCoin>,
    val walletValue: Double
)