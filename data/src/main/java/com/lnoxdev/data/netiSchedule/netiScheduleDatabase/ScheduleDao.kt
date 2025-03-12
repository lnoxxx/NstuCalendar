package com.lnoxdev.data.netiSchedule.netiScheduleDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleEntity): Long

    @Query("SELECT * FROM schedules WHERE id = 0")
    suspend fun getSchedule(): ScheduleEntity
}