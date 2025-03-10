package com.lnoxdev.data.models

import com.lnoxdev.data.models.lesson.Lesson

data class ScheduleDay(
    val dayOfWeek: Int,
    val lessons: List<Lesson>,
)
