package com.lnoxdev.data.models.weeklySchedule

import java.time.LocalDate
import java.time.LocalDateTime

data class WeeklySchedule(
    val group: String,
    val semester: String,
    val weeks: List<ScheduleWeek>,
    val saveTime: LocalDateTime,
    val startDate: LocalDate,
    val endDate: LocalDate,
)