package com.lnoxdev.data.netiSchedule.netiScheduleDataSource

import android.util.Log
import com.lnoxdev.data.InternetException
import com.lnoxdev.data.LoadWeekRangeException
import com.lnoxdev.data.ParseException
import com.lnoxdev.data.SaveException
import com.lnoxdev.data.Time
import com.lnoxdev.data.netiSchedule.netiScheduleDatabase.Schedule
import com.lnoxdev.data.netiSchedule.netiScheduleDatabase.ScheduleDao
import java.time.LocalDate

class NetiScheduleLoader(
    private val netiApi: NetiApi,
    private val scheduleDao: ScheduleDao,
) {

    suspend fun updateSchedule(group: String) {
        val dateRange = getScheduleDateRange(group)
        val scheduleHtml = loadScheduleHtml(group)
        val schedule = parseScheduleHtml(scheduleHtml, dateRange.first, dateRange.second)
        saveScheduleToDatabase(schedule)
    }

    private suspend fun loadScheduleHtml(group: String): String {
        try {
            val scheduleResponse = netiApi.getSchedule(group)
            val scheduleHtml =
                scheduleResponse.body()?.string() ?: throw InternetException("Failed load schedule")
            return scheduleHtml
        } catch (e: Exception) {
            Log.e("NetiScheduleLoader", e.toString())
            throw InternetException("Failed load schedule")
        }
    }

    private fun parseScheduleHtml(
        html: String,
        startDate: LocalDate,
        endDate: LocalDate
    ): Schedule {
        try {
            val schedule = HtmlScheduleParser.parseSchedule(html, startDate, endDate)
                ?: throw ParseException("Failed parse schedule")
            return schedule
        } catch (e: Exception) {
            Log.e("NetiScheduleLoader", e.toString())
            throw ParseException("Failed parse schedule")
        }
    }

    private suspend fun saveScheduleToDatabase(schedule: Schedule) {
        try {
            scheduleDao.insertSchedule(schedule)
        } catch (e: Exception) {
            Log.e("NetiScheduleLoader", e.toString())
            throw SaveException("Failed save schedule")
        }
    }

    private suspend fun getScheduleDateRange(group: String): Pair<LocalDate, LocalDate> {
        try {
            val scheduleHtml = netiApi.getScheduleWeek(group).body()?.string() ?: throw Exception()
            val weeksInfo = HtmlScheduleParser.parseScheduleWeek(scheduleHtml)
            val year = Time.getNowDate().year
            val startDate = LocalDate.of(year, weeksInfo.startMonth, weeksInfo.startDay)
            val endDate = startDate.plusWeeks(weeksInfo.weekCount.toLong()).minusDays(2)
            return startDate to endDate
        } catch (e: Exception) {
            Log.e("NetiScheduleLoader", e.toString())
            throw LoadWeekRangeException("Failed to load week range")
        }
    }
}
