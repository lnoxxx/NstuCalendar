package com.lnoxdev.data.gCalendarExport

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import com.lnoxdev.data.CALENDAR_ACCOUNT_NAME
import com.lnoxdev.data.CALENDAR_DISPLAY_NAME
import com.lnoxdev.data.CalendarAddingEventException
import com.lnoxdev.data.CalendarCreateException
import com.lnoxdev.data.CalendarDeleteException
import com.lnoxdev.data.CalendarPermissionRequired
import com.lnoxdev.data.GetScheduleException
import com.lnoxdev.data.R
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
    suspend fun export(settings: ExportSettings) {
        val calendarId = getOrCreateCalendar()
        withContext(Dispatchers.IO) {
            val schedule = netiScheduleRepository.weeklySchedule.first()
                ?: throw GetScheduleException("Schedule null!")
            for (week in schedule.weeks) {
                for (day in week.days) {
                    for (lesson in day.lessons) {
                        try {
                            inputLessonInCalendar(lesson, day, settings, calendarId)
                        } catch (e: Exception) {
                            throw CalendarAddingEventException("Failed to add event to calendar")
                        }
                    }
                }
            }
        }
    }

    private fun inputLessonInCalendar(
        lesson: Lesson,
        day: ScheduleDay,
        settings: ExportSettings,
        calendarId: Long
    ) {
        val lessonTime = day.date?.let { getLessonTime(lesson, it) } ?: return
        val lessonDescription = makeLessonDescription(lesson)
        val values = ContentValues().apply {
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
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

    private fun getOrCreateCalendar(): Long {
        checkCalendarPermission()

        val contentResolver = context.contentResolver
        val selection =
            "${CalendarContract.Calendars.ACCOUNT_NAME} = ? AND ${CalendarContract.Calendars.ACCOUNT_TYPE} = ?"
        val selectionArgs = arrayOf(CALENDAR_ACCOUNT_NAME, CalendarContract.ACCOUNT_TYPE_LOCAL)
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.ACCOUNT_NAME
        )
        val cursor = contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )
        cursor?.use {
            if (it.moveToFirst()) {
                val calIdIndex = it.getColumnIndex(CalendarContract.Calendars._ID)
                return it.getLong(calIdIndex)
            }
        }
        return createCalendar()
    }

    private fun checkCalendarPermission() {
        val readCalendarPermission =
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALENDAR)
        val writeCalendarPermission =
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_CALENDAR)
        if (readCalendarPermission != android.content.pm.PackageManager.PERMISSION_GRANTED ||
            writeCalendarPermission != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            throw CalendarPermissionRequired("Need calendar permission!")
        }
    }

    private fun createCalendar(): Long {
        val values = ContentValues().apply {
            put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDAR_ACCOUNT_NAME)
            put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
            put(CalendarContract.Calendars.NAME, CALENDAR_DISPLAY_NAME)
            put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_DISPLAY_NAME)
            put(
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER
            )
            put(
                CalendarContract.Calendars.CALENDAR_COLOR,
                ContextCompat.getColor(context, R.color.NstuCalendarColor)
            )
            put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDAR_ACCOUNT_NAME)
            put(CalendarContract.Calendars.VISIBLE, 1)
            put(CalendarContract.Calendars.SYNC_EVENTS, 0)
        }

        var uri = CalendarContract.Calendars.CONTENT_URI
        uri = uri.buildUpon()
            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDAR_ACCOUNT_NAME)
            .appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL
            )
            .build()

        val contentResolver = context.contentResolver
        val resultUri = contentResolver.insert(uri, values)

        return if (resultUri != null) {
            ContentUris.parseId(resultUri)
        } else {
            throw CalendarCreateException("Failed to create new calendar")
        }
    }

    fun deleteCalendar() {
        val calendarId = getOrCreateCalendar()
        val contentResolver = context.contentResolver
        val calendarUri =
            ContentUris.withAppendedId(CalendarContract.Calendars.CONTENT_URI, calendarId)
        val deleteUri = calendarUri.buildUpon()
            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDAR_ACCOUNT_NAME)
            .appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL
            ).build()

        try {
            contentResolver.delete(deleteUri, null, null)
        } catch (e: SecurityException) {
            throw CalendarDeleteException("Failed to delete calendar")
        }
    }

    companion object {
        const val NOVOSIBIRSK_TIME_ZONE = "Asia/Novosibirsk"
    }
}