package com.lnoxdev.data.gCalendarExport

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.CalendarContract
import com.lnoxdev.data.GetScheduleException
import com.lnoxdev.data.models.gCalendarExport.ExportSettings
import com.lnoxdev.data.models.schedule.ScheduleDay
import com.lnoxdev.data.models.schedule.lesson.Lesson
import com.lnoxdev.data.models.schedule.lesson.LessonType
import com.lnoxdev.data.neti.NetiScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId

class GCalendarExportManager(
    private val context: Context,
    private val netiScheduleRepository: NetiScheduleRepository,
) {
    suspend fun export(settings: ExportSettings): Boolean {
        return withContext(Dispatchers.IO) {
            val schedule = netiScheduleRepository.weeklySchedule.first()
                ?: throw GetScheduleException("Schedule null!")
            for (week in schedule.weeks) {
                for (day in week.days) {
                    for (lesson in day.lessons) {
                        inputLessonInCalendar(lesson, day, settings)
                    }
                }
            }
            true
        }
    }

    private fun inputLessonInCalendar(lesson: Lesson, day: ScheduleDay, settings: ExportSettings) {
        val lessonTime = day.date?.let { getLessonTime(lesson, it) } ?: return
        val lessonDescription = makeLessonDescription(lesson)
        val values = ContentValues().apply {
            put(CalendarContract.Events.CALENDAR_ID, settings.calendarId)
            put(CalendarContract.Events.EVENT_TIMEZONE, NOVOSIBIRSK_TIME_ZONE)
            put(CalendarContract.Events.TITLE, lesson.name)
            put(CalendarContract.Events.DTSTART, lessonTime.first)
            put(CalendarContract.Events.DTEND, lessonTime.second)
            put(CalendarContract.Events.DESCRIPTION, lessonDescription)
        }
        val uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
        val eventID = uri?.lastPathSegment?.toLong()
        eventID?.let { settings.reminderMinutes?.let { minutes -> addLessonReminder(it, minutes) } }
    }

    private fun addLessonReminder(lessonId: Long, reminderMinutes: Int) {
        val values = ContentValues().apply {
            put(CalendarContract.Reminders.MINUTES, reminderMinutes)
            put(CalendarContract.Reminders.EVENT_ID, lessonId)
            put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
        }
        context.contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, values)
    }

    private fun getLessonTime(lesson: Lesson, date: LocalDate): Pair<Long, Long> {
        val lessonDateTimeStart = date.atTime(lesson.time.timeStart)
        val zonedDateTimeStart = lessonDateTimeStart.atZone(ZoneId.of(NOVOSIBIRSK_TIME_ZONE))
        val timeInMillisStart = zonedDateTimeStart.toInstant().toEpochMilli()
        val lessonDateTimeEnd = date.atTime(lesson.time.timeEnd)
        val zonedDateTimeEnd = lessonDateTimeEnd.atZone(ZoneId.of(NOVOSIBIRSK_TIME_ZONE))
        val timeInMillisEnd = zonedDateTimeEnd.toInstant().toEpochMilli()
        return timeInMillisStart to timeInMillisEnd
    }

    private fun makeLessonDescription(lesson: Lesson): String {
        val textType = when (lesson.type) {
            LessonType.LECTURE -> "Лекция"
            LessonType.PRACTICE -> "Практика"
            LessonType.LABORATORY -> "Лабораторная"
            LessonType.OTHER -> "Занятие"
        }
        val textCabinet = lesson.cabinet ?: "Неизвестно"
        val textTeacherName = lesson.teacher?.name ?: "Неизвестно"
        val description = "$textType | $textCabinet | $textTeacherName"
        return description
    }

    fun getCalendarList(): Map<Long, String> {
        val calendars = mutableMapOf<Long, String>()
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        )
        val cursor: Cursor? = context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(CalendarContract.Calendars._ID))
                val name =
                    it.getString(it.getColumnIndexOrThrow(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME))
                calendars[id] = name
            }
        }
        return calendars
    }

    companion object {
        const val NOVOSIBIRSK_TIME_ZONE = "Asia/Novosibirsk"
    }
}