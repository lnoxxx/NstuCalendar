package com.lnoxdev.nstucalendarparcer.utils

import com.lnoxdev.nstucalendarparcer.R

fun getMonthResources(month: Int): Int {
    return when (month) {
        1 -> R.string.month_january
        2 -> R.string.month_february
        3 -> R.string.month_march
        4 -> R.string.month_april
        5 -> R.string.month_may
        6 -> R.string.month_june
        7 -> R.string.month_july
        8 -> R.string.month_august
        9 -> R.string.month_september
        10 -> R.string.month_october
        11 -> R.string.month_november
        12 -> R.string.month_december
        else -> R.string.month_unknown
    }
}