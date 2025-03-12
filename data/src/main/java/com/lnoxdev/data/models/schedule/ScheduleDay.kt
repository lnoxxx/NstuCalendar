package com.lnoxdev.data.models.schedule

import com.lnoxdev.data.models.lesson.Lesson

data class ScheduleDay(
    val dayOfWeek: Int,
    val lessons: List<Lesson>,
)
