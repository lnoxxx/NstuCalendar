package com.lnoxdev.data.models.lesson

import kotlinx.serialization.Serializable

@Serializable
enum class LessonType {
    LECTURE, PRACTICE, LABORATORY, OTHER,
}