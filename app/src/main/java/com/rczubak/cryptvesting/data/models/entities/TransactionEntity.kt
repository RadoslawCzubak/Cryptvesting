package com.rczubak.cryptvesting.data.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.rczubak.cryptvesting.utils.roomUtils.Converters
import java.time.LocalDateTime

@Entity
@TypeConverters(Converters::class)
data class TransactionEntity(
    @PrimaryKey                     val id: String,
    @ColumnInfo(name = "buyCoin")   val buyCoin: String,
    @ColumnInfo(name = "sellCoin")  val sellCoin: String,
    @ColumnInfo(name = "price")     val price: Double,
    @ColumnInfo(name = "amount")    val amount: Double,
    @ColumnInfo(name = "type")      val type: TransactionType,
    @ColumnInfo(name = "date")      val date: LocalDateTime
)

enum class TransactionType {
    BUY, SELL
}