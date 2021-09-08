package com.rczubak.cryptvesting.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rczubak.cryptvesting.domain.model.TransactionModel
import com.rczubak.cryptvesting.domain.model.TransactionType
import com.rczubak.cryptvesting.utils.roomUtils.Converters
import org.threeten.bp.LocalDateTime

@Entity(tableName = "transactions")
@TypeConverters(Converters::class)
data class TransactionEntity(
    @ColumnInfo(name = "buyCoin") val buyCoin: String,
    @ColumnInfo(name = "sellCoin") val sellCoin: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "type") val type: TransactionType,
    @ColumnInfo(name = "date") val date: LocalDateTime
) {
    @PrimaryKey var id: Int = "$buyCoin$sellCoin$price$amount$type$date".hashCode()

    constructor(transaction: TransactionModel) : this(
        transaction.buyCoin,
        transaction.sellCoin,
        transaction.price,
        transaction.amount,
        transaction.type,
        transaction.date
    )
}