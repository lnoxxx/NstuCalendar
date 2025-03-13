package com.lnoxdev.data.netiSchedule.netiScheduleDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Schedule::class], version = 1)
@TypeConverters(ScheduleConverters::class)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
}