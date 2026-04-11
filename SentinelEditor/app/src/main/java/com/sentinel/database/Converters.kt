package com.sentinel.database

import androidx.room.TypeConverter
import com.sentinel.model.GitHubAuth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            val instant = java.time.Instant.ofEpochMilli(value)
            LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault())
        }
    }
    
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): Long? {
        return dateTime?.atZone(java.time.ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }
    
    @TypeConverter
    fun fromTimestampStr(value: String?): LocalDateTime? {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        return value?.let {
            LocalDateTime.parse(it, formatter)
        }
    }
    
    @TypeConverter
    fun fromLocalDateTimeStr(dateTime: LocalDateTime?): String? {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        return dateTime?.format(formatter)
    }
}
