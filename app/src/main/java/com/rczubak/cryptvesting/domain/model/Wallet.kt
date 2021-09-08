package com.rczubak.cryptvesting.domain.model

data class Wallet (
    val walletCoins: List<WalletCoin>,
    val walletValue: Double
)