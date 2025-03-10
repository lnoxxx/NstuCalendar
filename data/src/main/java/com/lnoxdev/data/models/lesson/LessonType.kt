package com.lnoxdev.data.models.lesson

enum class LessonType(val sourceName: String) {
    LECTURE("Лекция"),
    PRACTICE("Практика"),
    LABORATORY("Лабораторная"),
    OTHER(""),
}