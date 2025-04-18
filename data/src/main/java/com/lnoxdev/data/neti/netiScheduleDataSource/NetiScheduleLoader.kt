package com.lnoxdev.data.neti.netiScheduleDataSource

import android.util.Log
import com.lnoxdev.data.InternetException
import com.lnoxdev.data.ParseException
import com.lnoxdev.data.SaveException
import com.lnoxdev.data.Time
import com.lnoxdev.data.neti.NetiApi
import com.lnoxdev.data.neti.netiScheduleDatabase.Schedule
import com.lnoxdev.data.neti.netiScheduleDatabase.ScheduleDao
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
        val scheduleHtml = loadScheduleWeekHtml(group)
        val weeksInfo = parseScheduleWeekHtml(scheduleHtml)
        val year = Time.getNowDateTime().year
        val startDate = LocalDate.of(year, weeksInfo.startMonth, weeksInfo.startDay)
        val endDate = startDate.plusWeeks(weeksInfo.weekCount.toLong()).minusDays(2)
        return startDate to endDate
    }

    private suspend fun loadScheduleWeekHtml(group: String): String {
        try {
            return netiApi.getScheduleWeek(group).body()?.string() ?: throw Exception()
        } catch (e: Exception) {
            Log.e("NetiScheduleLoader", e.toString())
            throw InternetException("Failed load schedule")
        }
    }

    private fun parseScheduleWeekHtml(scheduleHtml: String): WeeksParseResult {
        try {
            return HtmlScheduleParser.parseScheduleWeek(scheduleHtml)
        } catch (e: Exception) {
            Log.e("NetiScheduleLoader", e.toString())
            throw ParseException("Failed parse schedule")
        }
    }
}
