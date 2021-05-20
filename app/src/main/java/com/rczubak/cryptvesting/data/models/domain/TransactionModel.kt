package com.rczubak.cryptvesting.data.models.domain

import java.time.LocalDateTime

data class TransactionModel(
    val id: String,
    val buyCoin: String,
    val sellCoin: String,
    val price: Double,
    val amount: Double,
    val type: TransactionType,
    val date: LocalDateTime
) {
    private fun getTotalPrice(): Double {
        return price * amount
    }
}

enum class TransactionType {
    BUY, SELL
}