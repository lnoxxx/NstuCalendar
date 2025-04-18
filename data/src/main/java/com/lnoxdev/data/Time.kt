package com.lnoxdev.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

object Time {
    fun getNowDateTime(): LocalDateTime = LocalDateTime.now()

    fun getCurrentDateTimeFlow(): Flow<LocalDateTime> = flow {
        var currentDateTime = LocalDateTime.now()
        emit(currentDateTime)

        while (true) {
            delay(3000)
            currentDateTime = LocalDateTime.now()
            emit(currentDateTime)
        }
    }
}