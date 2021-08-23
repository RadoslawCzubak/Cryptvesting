package com.rczubak.cryptvesting.data.models.domain

import androidx.room.PrimaryKey
import com.rczubak.cryptvesting.data.models.entities.TransactionEntity
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

data class TransactionModel(
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

            return TransactionModel(buyCoin, sellCoin, objPrice, objAmount, objType, objDate)
        }
    }

    @PrimaryKey var id: Int =  "$buyCoin$sellCoin$price$amount$type$date".hashCode()


    constructor(transaction: TransactionEntity) : this(
        transaction.buyCoin,
        transaction.sellCoin,
        transaction.price,
        transaction.amount,
        transaction.type,
        transaction.date
    )

    fun getTotalPrice(): Double {
        return price * amount
    }
}

enum class TransactionType {
    BUY, SELL
}