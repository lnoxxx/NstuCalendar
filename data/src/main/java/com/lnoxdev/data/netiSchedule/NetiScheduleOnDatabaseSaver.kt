package com.lnoxdev.data.netiSchedule

import android.util.Log
import com.lnoxdev.data.SaveException
import com.lnoxdev.data.Time
import com.lnoxdev.data.models.schedule.Schedule
import com.lnoxdev.data.netiSchedule.netiScheduleDatabase.ScheduleDao
import com.lnoxdev.data.toScheduleEntity

class NetiScheduleOnDatabaseSaver(private val scheduleDao: ScheduleDao) {
    suspend fun saveScheduleToDatabase(schedule: Schedule) {
        try {
            scheduleDao.insertSchedule(schedule.toScheduleEntity(Time.getNowTime()))
        } catch (e: Exception) {
            Log.e("NetiScheduleOnDatabaseSaver", e.toString())
            throw SaveException("Failed to save schedule")
        }
    }

    suspend fun TESTFUN() = scheduleDao.getSchedule()

}