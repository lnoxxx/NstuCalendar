package com.lnoxdev.data.models.lesson

import kotlinx.serialization.Serializable

@Serializable
enum class LessonDateType {
    EVEN, ODD, UNIQUE, ALL,
}