package com.lnoxdev.nstucalendarparcer.utils

import java.time.format.DateTimeFormatter

fun getDateTimeFormatter(is12HourTimeFormat: Boolean): DateTimeFormatter{
    return if (is12HourTimeFormat)
        DateTimeFormatter.ofPattern("dd.MM.yy hh:mm a")
    else
        DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
}

fun getTimeFormat(is12HourTimeFormat: Boolean): DateTimeFormatter{
    return if (is12HourTimeFormat)
        DateTimeFormatter.ofPattern("hh:mm a")
    else
        DateTimeFormatter.ofPattern("HH:mm")
}