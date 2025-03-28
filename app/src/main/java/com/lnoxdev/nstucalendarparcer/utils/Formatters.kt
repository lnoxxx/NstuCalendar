package com.lnoxdev.nstucalendarparcer.utils

import java.time.format.DateTimeFormatter

fun getDateFormatter(): DateTimeFormatter {
    return DateTimeFormatter.ofPattern("MM-dd")
}