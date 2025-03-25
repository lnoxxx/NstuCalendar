package com.lnoxdev.data.models.schedule.lesson

import kotlinx.serialization.Serializable

@Serializable
enum class LessonType {
    LECTURE, PRACTICE, LABORATORY, OTHER,
}