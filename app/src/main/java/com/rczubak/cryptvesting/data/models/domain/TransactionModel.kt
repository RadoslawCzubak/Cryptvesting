package com.rczubak.cryptvesting.data.models.domain

import org.threeten.bp.format.DateTimeFormatter
import java.lang.IllegalArgumentException
import org.threeten.bp.LocalDateTime

data class TransactionModel(
    val id: String,
    val buyCoin: String,
    val sellCoin: String,
    val price: Double,
    val amount: Double,
    val type: TransactionType,
    val date: LocalDateTime
) {

    companion object {
        @Throws(IllegalArgumentException::class)
        fun createFromStrings(
            id: String,
            buyCoin: String,
            sellCoin: String,
            price: String,
            amount: String,
            type: String,
            date: String
        ): TransactionModel {
            val objPrice: Double = price.toDoubleOrNull() ?: throw IllegalArgumentException()
            val objAmount: Double = amount.toDoubleOrNull() ?: throw IllegalArgumentException()
            val objType = TransactionType.valueOf(type)
            val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val objDate = LocalDateTime.parse(date, df)

            return TransactionModel(id, buyCoin, sellCoin, objPrice, objAmount, objType, objDate)
        }
    }

    private fun getTotalPrice(): Double {
        return price * amount
    }
}

enum class TransactionType {
    BUY, SELL
}