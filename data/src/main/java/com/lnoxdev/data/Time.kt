package com.lnoxdev.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object Time {
    fun getNowDateTime(): LocalDateTime = LocalDateTime.now()
    fun getNowDate(): LocalDate = LocalDate.now()

    fun getCurrentDateTimeFlow(): Flow<LocalDateTime> = flow {
        var currentDateTime = LocalDateTime.now()
        emit(currentDateTime)

        while (true) {
            val nextMinute = currentDateTime.plusMinutes(1).truncatedTo(ChronoUnit.MINUTES)
            val delayMillis = ChronoUnit.MILLIS.between(LocalDateTime.now(), nextMinute)
            delay(delayMillis)

            currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
            emit(currentDateTime)
        }
    }
}