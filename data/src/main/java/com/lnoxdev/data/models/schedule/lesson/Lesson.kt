package com.lnoxdev.data.models.schedule.lesson

import com.lnoxdev.data.models.schedule.Teacher
import kotlinx.serialization.Serializable

@Serializable
data class Lesson(
    val name: String,
    val dateType: LessonDateType,
    val dateUniqueWeeks: List<Int>?,
    val teacher: Teacher?,
    val cabinet: String?,
    val type: LessonType,
    val time: LessonTime,
)
