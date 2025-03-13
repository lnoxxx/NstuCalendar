package com.lnoxdev.data

import java.time.LocalDate
import java.time.LocalDateTime

object Time {
    fun getNowDateTime(): LocalDateTime = LocalDateTime.now()
    fun getNowDate(): LocalDate = LocalDate.now()
}