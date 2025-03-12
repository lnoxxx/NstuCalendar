package com.lnoxdev.data.models.schedule

import com.lnoxdev.data.models.lesson.Lesson
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleDay(
    val dayOfWeek: Int,
    val lessons: List<Lesson>,
)
