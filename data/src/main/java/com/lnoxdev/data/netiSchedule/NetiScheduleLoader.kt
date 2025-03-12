package com.lnoxdev.data.netiSchedule

import com.lnoxdev.data.InternetException
import com.lnoxdev.data.ParseException
import com.lnoxdev.data.models.schedule.Schedule
import com.lnoxdev.data.netiNetworkDataSource.NetiApi

class NetiScheduleLoader(
    private val netiApi: NetiApi,
    private val saver: NetiScheduleOnDatabaseSaver
) {

    suspend fun updateSchedule(group: String) {
        try {
            val scheduleHtml = loadScheduleHtml(group)
            val schedule = parseScheduleHtml(scheduleHtml)
            saver.saveScheduleToDatabase(schedule)
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun loadScheduleHtml(group: String): String {
        try {
            val scheduleResponse = netiApi.getSchedule(group)
            return scheduleResponse.body()?.string() ?: throw Exception()
        } catch (e: Exception) {
            throw InternetException("Failed to get schedule")
        }
    }

    private fun parseScheduleHtml(html: String): Schedule {
        try {
            val schedule = HtmlScheduleParser.parseSchedule(html) ?: throw Exception()
            return schedule
        } catch (e: Exception) {
            throw ParseException("Failed to parse schedule")
        }
    }

    suspend fun TESTFUN () = saver.TESTFUN()
}
