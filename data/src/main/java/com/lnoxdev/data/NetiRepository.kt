package com.lnoxdev.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetiRepository(private val netiApi: NetiApi) {

    suspend fun getSchedule() {
        withContext(Dispatchers.IO) {
            val response = netiApi.getSchedule("АБс-222")
        }
    }
}