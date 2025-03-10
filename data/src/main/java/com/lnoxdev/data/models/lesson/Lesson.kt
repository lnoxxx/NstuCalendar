package com.lnoxdev.data.models.lesson

import com.lnoxdev.data.models.Teacher

data class Lesson(
    val name: String,
    val dateType: LessonDateType,
    val dateUniqueWeeks: List<Int>?,
    val teacher: Teacher,
    val cabinet: String?,
    val type: LessonType,
    val time: LessonTime,
)
