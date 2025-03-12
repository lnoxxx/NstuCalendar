package com.lnoxdev.data.models.lesson

import kotlinx.serialization.Serializable

@Serializable
enum class LessonType(val sourceName: String) {
    LECTURE("Лекция"),
    PRACTICE("Практика"),
    LABORATORY("Лабораторная"),
    OTHER(""),
}