package com.lnoxdev.data.netiSchedule

import com.lnoxdev.data.SettingGroupException
import com.lnoxdev.data.appSettings.SettingsRepository
import com.lnoxdev.data.models.lesson.Lesson
import com.lnoxdev.data.models.lesson.LessonDateType
import com.lnoxdev.data.models.schedule.ScheduleDay
import com.lnoxdev.data.models.weeklySchedule.ScheduleWeek
import com.lnoxdev.data.models.weeklySchedule.WeeklySchedule
import com.lnoxdev.data.netiSchedule.netiScheduleDataSource.NetiScheduleLoader
import com.lnoxdev.data.netiSchedule.netiScheduleDatabase.Schedule
import com.lnoxdev.data.netiSchedule.netiScheduleDatabase.ScheduleDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class NetiScheduleRepository(
    scheduleDao: ScheduleDao,
    private val netiScheduleLoader: NetiScheduleLoader,
    private val settingsRepository: SettingsRepository,
) {
    val schedule: Flow<Schedule?> = scheduleDao.getSchedule()
    val weeklySchedule: Flow<WeeklySchedule?> =
        scheduleDao.getSchedule().map { scheduleToWeeklySchedule(it) }

    suspend fun updateSchedule() {
        withContext(Dispatchers.IO) {
            val group =
                settingsRepository.group.first() ?: throw SettingGroupException("Group not found")
            netiScheduleLoader.updateSchedule(group)
        }
    }

    private fun scheduleToWeeklySchedule(schedule: Schedule): WeeklySchedule {
        var iteratorWeek = schedule.startDate
        var weekCounter = 1
        // result weeks list
        val weeks = mutableListOf<ScheduleWeek>()
        while (iteratorWeek.isBefore(schedule.endDate)) {
            var iteratorDay = iteratorWeek
            // result days list
            val days = mutableListOf<ScheduleDay>()
            while (iteratorDay.dayOfWeek.value <= 6) {
                // day in schedule
                val scheduleDay = schedule.days[iteratorDay.dayOfWeek.value - 1]
                // result day list
                val lessons = mutableListOf<Lesson>()
                for (lesson in scheduleDay.lessons) {
                    val isEvenWeek = weekCounter % 2 == 0
                    val needAddLesson = when (lesson.dateType) {
                        LessonDateType.EVEN -> isEvenWeek
                        LessonDateType.ODD -> !isEvenWeek
                        LessonDateType.UNIQUE -> lesson.dateUniqueWeeks!!.contains(weekCounter)
                        LessonDateType.ALL -> true
                    }
                    if (needAddLesson) lessons.add(lesson)
                }
                // add day in days
                days.add(ScheduleDay(dayOfWeek = iteratorDay.dayOfWeek.value, lessons = lessons))
                iteratorDay = iteratorDay.plusDays(1)
            }
            // add week in weeks
            weeks.add(ScheduleWeek(days))
            weekCounter += 1
            iteratorWeek = iteratorWeek.plusWeeks(1)
        }
        // result
        return WeeklySchedule(
            group = schedule.group,
            semester = schedule.semester,
            weeks = weeks,
            saveTime = schedule.saveTime,
            startDate = schedule.startDate,
            endDate = schedule.endDate,
        )
    }
}