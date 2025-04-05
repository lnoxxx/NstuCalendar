package com.lnoxdev.data.neti.netiScheduleDatabase

import androidx.room.TypeConverter
import com.lnoxdev.data.models.schedule.ScheduleDay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime

class ScheduleConverters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }

    @TypeConverter
    fun toLocalDateTime(dateTime: String): LocalDateTime {
        return LocalDateTime.parse(dateTime)
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun toLocalDate(date: String): LocalDate {
        return LocalDate.parse(date)
    }

    @TypeConverter
    fun fromScheduleDayList(scheduleDays: List<ScheduleDay>): String {
        return json.encodeToString(scheduleDays)
    }

    @TypeConverter
    fun toScheduleDayList(jsonString: String): List<ScheduleDay> {
        return json.decodeFromString(jsonString)
    }
}