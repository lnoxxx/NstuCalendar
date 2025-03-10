package com.lnoxdev.data.models.lesson

enum class LessonDateType(val sourceName: String) {
    EVEN("по чётным"),
    ODD("по нечётным"),
    UNIQUE("недели"),
    ALL(""),
}