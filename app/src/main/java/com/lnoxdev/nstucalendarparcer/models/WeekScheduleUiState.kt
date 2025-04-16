package com.lnoxdev.nstucalendarparcer.models

import com.lnoxdev.nstucalendarparcer.R
import java.time.LocalDate
import java.time.LocalTime

data class WeekScheduleUiState(
    val weekIndex: Int,
    val schedule: List<WeekScheduleItem>?,
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
    val index: Int,
    val is12HourTimeFormat: Boolean,
) : WeekScheduleItem()

data class WeekScheduleDate(
    val date: LocalDate,
    val dayOfWeek: Int,
    val isFinished: Boolean,
    val isToday: Boolean,
) : WeekScheduleItem()

data class WeekScheduleNowLessonTime(
    val timeStart: LocalTime,
    val timeEnd: LocalTime,
    val progress: Int,
    val is12HourTimeFormat: Boolean,
) : WeekScheduleItem()

data class WeekScheduleTeacher(
    val name: String?,
    val url: String?,
)

enum class WeekScheduleLessonType(val stringResource: Int, val colorResId: Int?) {
    LECTURE(R.string.lesson_type_lecture, R.color.colorLecture),
    PRACTICE(R.string.lesson_type_practice, R.color.colorPractice),
    LABORATORY(R.string.lesson_type_laboratory, R.color.colorLaboratory),
    OTHER(R.string.lesson_type_other, null),
}