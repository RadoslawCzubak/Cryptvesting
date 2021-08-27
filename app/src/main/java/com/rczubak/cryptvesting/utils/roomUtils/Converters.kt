package com.rczubak.cryptvesting.utils.roomUtils

import androidx.room.TypeConverter
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class Converters {
    @TypeConverter
    fun fromDatabaseDate(value: String): LocalDateTime? {
        if (value=="") return null
        val dateFormatter = DateTimeFormatter.ISO_DATE_TIME
        return LocalDateTime.parse(value, dateFormatter)
    }

    @TypeConverter
    fun toDatabaseDate(value: LocalDateTime): String {
        val dateFormatter = DateTimeFormatter.ISO_DATE_TIME
        return value.format(dateFormatter)?:""
    }
}