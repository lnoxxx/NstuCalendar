package com.lnoxdev.nstucalendarparcer.models

import java.time.LocalDateTime

data class WeeklyScheduleState(
    val lastUpdateTime: LocalDateTime?,
    val weeksCount: Int?,
    val isUpdate: Boolean = false,
    val exception: UiWeeklyScheduleExceptions? = null,
)

enum class UiWeeklyScheduleExceptions {
    INTERNET, PARSE, SAVE, SETTING_GROUP, UNKNOWN,
}
