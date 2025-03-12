package com.lnoxdev.data.netiSchedule.netiScheduleDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lnoxdev.data.models.schedule.ScheduleDay
import java.time.LocalTime

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey val id: Long = 0,
    val group: String,
    val semester: String,
    val days: List<ScheduleDay>,
    val saveTime: LocalTime,
)