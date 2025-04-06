package com.lnoxdev.nstucalendarparcer.models

import java.time.LocalDateTime

data class WeeklyScheduleState(
    val lastUpdateTime: LocalDateTime?,
    val weeksCount: Int?,
    val group: String?,
    val nowWeekIndex: Int?,
)
