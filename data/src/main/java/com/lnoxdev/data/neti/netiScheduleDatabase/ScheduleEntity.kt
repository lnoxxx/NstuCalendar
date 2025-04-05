package com.lnoxdev.data.neti.netiScheduleDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lnoxdev.data.models.schedule.ScheduleDay
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "schedules")
data class Schedule(
    @PrimaryKey val id: Long = 0,
    val group: String,
    val semester: String,
    val days: List<ScheduleDay>,
    val saveTime: LocalDateTime,
    val startDate: LocalDate,
    val endDate: LocalDate,
)