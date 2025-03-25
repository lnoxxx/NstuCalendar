package com.lnoxdev.data.models.schedule.lesson

import kotlinx.serialization.Serializable

@Serializable
enum class LessonDateType {
    EVEN, ODD, UNIQUE, ALL,
}