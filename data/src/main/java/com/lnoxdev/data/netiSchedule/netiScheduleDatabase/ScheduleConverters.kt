package com.lnoxdev.data.netiSchedule.netiScheduleDatabase

import androidx.room.TypeConverter
import com.lnoxdev.data.models.Teacher
import com.lnoxdev.data.models.lesson.Lesson
import com.lnoxdev.data.models.lesson.LessonDateType
import com.lnoxdev.data.models.lesson.LessonType
import com.lnoxdev.data.models.schedule.ScheduleDay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalTime

class ScheduleConverters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromLocalTime(time: LocalTime): String {
        return time.toString()
    }

    @TypeConverter
    fun toLocalTime(time: String): LocalTime {
        return LocalTime.parse(time)
    }

    @TypeConverter
    fun fromLessonDateType(type: LessonDateType): String {
        return type.name
    }

    @TypeConverter
    fun toLessonDateType(type: String): LessonDateType {
        return when (type) {
            LessonDateType.EVEN.name -> LessonDateType.EVEN
            LessonDateType.ODD.name -> LessonDateType.ODD
            LessonDateType.UNIQUE.name -> LessonDateType.UNIQUE
            else -> LessonDateType.ALL
        }
    }

    @TypeConverter
    fun fromLessonType(type: LessonType): String {
        return type.name
    }

    @TypeConverter
    fun toLessonType(type: String): LessonType {
        return when (type) {
            LessonType.LABORATORY.name -> LessonType.LABORATORY
            LessonType.LECTURE.name -> LessonType.LECTURE
            LessonType.PRACTICE.name -> LessonType.PRACTICE
            else -> LessonType.OTHER
        }
    }

    @TypeConverter
    fun fromIntList(list: List<Int>?): String? {
        if (list == null) return null
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toIntList(jsonString: String?): List<Int>? {
        if (jsonString == null) return null
        return json.decodeFromString(jsonString)
    }

    @TypeConverter
    fun fromTeacher(teacher: Teacher?): String? {
        return teacher?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toTeacher(jsonString: String?): Teacher? {
        return jsonString?.let { json.decodeFromString(jsonString) }
    }

    @TypeConverter
    fun fromLesson(lesson: Lesson): String {
        return json.encodeToString(lesson)
    }

    @TypeConverter
    fun toLesson(jsonString: String): Lesson {
        return json.decodeFromString(jsonString)
    }

    @TypeConverter
    fun fromLessonList(lessons: List<Lesson>): String {
        return json.encodeToString(lessons)
    }

    @TypeConverter
    fun toLessonList(jsonString: String): List<Lesson> {
        return json.decodeFromString(jsonString)
    }

    @TypeConverter
    fun fromScheduleDay(scheduleDay: ScheduleDay): String {
        return json.encodeToString(scheduleDay)
    }

    @TypeConverter
    fun toScheduleDay(jsonString: String): ScheduleDay {
        return json.decodeFromString(jsonString)
    }

    @TypeConverter
    fun fromScheduleDayList(scheduleDays: List<ScheduleDay>): String {
        return json.encodeToString(scheduleDays)
    }

    @TypeConverter
    fun toScheduleDayList(jsonString: String): List<ScheduleDay> {
        return json.decodeFromString(jsonString)
    }
}