package com.lnoxdev.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetiRepository(private val netiApi: NetiApi) {

    suspend fun getSchedule() {
        withContext(Dispatchers.IO) {
            val response = netiApi.getSchedule("АБс-223")
            val htmlString = response.body()?.string()
            htmlString?.let {
                val schedule = HtmlScheduleParser.parseSchedule(it)
                schedule?.let { sch ->
                    Log.d("testlog", "Group: ${sch.group}")
                    Log.d("testlog", "Semestr: ${sch.semester}")
                    for (day in sch.days) {
                        Log.d("testlog", "Day of week: ${day.dayOfWeek}")
                        for (lesson in day.lessons) {
                            Log.d("testlog", "           $lesson")
                        }
                    }
                }
            }
        }
    }
}