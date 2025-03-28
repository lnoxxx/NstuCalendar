package com.lnoxdev.data.netiSchedule

import com.lnoxdev.data.SettingGroupException
import com.lnoxdev.data.appSettings.SettingsManager
import com.lnoxdev.data.models.schedule.lesson.Lesson
import com.lnoxdev.data.models.schedule.lesson.LessonDateType
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
    private val settingsManager: SettingsManager,
) {
    private val schedule: Flow<Schedule?> = scheduleDao.getSchedule()

    val weeklySchedule: Flow<WeeklySchedule?> =
        schedule.map { schedule -> schedule?.let { scheduleToWeeklySchedule(it) } }

    fun getWeekSchedule(weekNumber: Int): Flow<ScheduleWeek?> {
        return weeklySchedule.map { it?.weeks?.getOrNull(weekNumber) }
    }

    suspend fun updateSchedule() {
        withContext(Dispatchers.IO) {
            val group =
                settingsManager.group.first() ?: throw SettingGroupException("Group not found")
            netiScheduleLoader.updateSchedule(group)
        }
    }

    private fun scheduleToWeeklySchedule(schedule: Schedule): WeeklySchedule {
        var iteratorWeek = schedule.startDate
        var weekCounter = 1
        val weeks = mutableListOf<ScheduleWeek>()
        while (iteratorWeek.isBefore(schedule.endDate)) {
            var iteratorDay = iteratorWeek
            val days = mutableListOf<ScheduleDay>()
            while (iteratorDay.dayOfWeek.value <= 6) {
                val scheduleDay = schedule.days[iteratorDay.dayOfWeek.value - 1]
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
                days.add(
                    ScheduleDay(
                        dayOfWeek = iteratorDay.dayOfWeek.value,
                        lessons = lessons,
                        date = iteratorDay
                    )
                )
                iteratorDay = iteratorDay.plusDays(1)
            }
            weeks.add(ScheduleWeek(days))
            weekCounter += 1
            iteratorWeek = iteratorWeek.plusWeeks(1)
        }
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