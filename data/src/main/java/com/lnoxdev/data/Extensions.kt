package com.lnoxdev.data

import com.lnoxdev.data.models.schedule.Schedule
import com.lnoxdev.data.netiSchedule.netiScheduleDatabase.ScheduleEntity
import java.time.LocalTime

fun Schedule.toScheduleEntity(time: LocalTime): ScheduleEntity {
    return ScheduleEntity(
        group = this.group,
        semester = this.semester,
        days = this.days,
        saveTime = time,
    )
}

fun ScheduleEntity.toSchedule(): Schedule {
    return Schedule(
        group = this.group,
        semester = this.semester,
        days = this.days,
    )
}