package com.lnoxdev.nstucalendarparcer.utils

import com.lnoxdev.nstucalendarparcer.R

fun getDayOfWeekResource(dayOfWeek: Int): Int {
    return when (dayOfWeek) {
        1 -> R.string.day_of_week_monday
        2 -> R.string.day_of_week_tuesday
        3 -> R.string.day_of_week_wednesday
        4 -> R.string.day_of_week_thursday
        5 -> R.string.day_of_week_friday
        6 -> R.string.day_of_week_saturday
        7 -> R.string.day_of_week_sunday
        else -> R.string.day_of_week_unknown
    }
}