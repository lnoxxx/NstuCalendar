package com.lnoxdev.data.models

data class Schedule(
    val group: String,
    val semester: String,
    val days: List<ScheduleDay>,
)
