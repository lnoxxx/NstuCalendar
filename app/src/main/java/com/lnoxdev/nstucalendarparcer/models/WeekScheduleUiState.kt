package com.lnoxdev.nstucalendarparcer.models

import java.time.LocalDate
import java.time.LocalTime

data class WeekScheduleUiState(
    val weekIndex: Int,
    val schedule: List<WeekScheduleItem>
)

sealed class WeekScheduleItem

data class WeekScheduleLesson(
    val name: String,
    val teacher: WeekScheduleTeacher?,
    val cabinet: String?,
    val type: WeekScheduleLessonType,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val isFinished: Boolean,
) : WeekScheduleItem()

data class WeekScheduleDate(
    val date: LocalDate,
    val dayOfWeek: Int,
    val isFinished: Boolean,
) : WeekScheduleItem()

data class WeekScheduleNowLessonTime(
    val timeStart: LocalTime,
    val timeEnd: LocalTime,
    val progress: Int,
) : WeekScheduleItem()

data class WeekScheduleTeacher(
    val name: String?,
    val url: String?,
)

enum class WeekScheduleLessonType {
    LECTURE, PRACTICE, LABORATORY, OTHER,
}