package com.lnoxdev.data

import android.util.Log
import com.lnoxdev.data.models.Schedule
import com.lnoxdev.data.models.ScheduleDay
import com.lnoxdev.data.models.lesson.Lesson
import com.lnoxdev.data.models.lesson.LessonDateType
import com.lnoxdev.data.models.lesson.LessonType
import com.lnoxdev.data.models.Teacher
import com.lnoxdev.data.models.lesson.LessonTime
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.time.LocalTime

object HtmlScheduleParser {

    fun parseSchedule(stringHtml: String): Schedule? {
        val doc = Jsoup.parse(stringHtml)
        val days = doc.select(".schedule__table-row[data-empty]").toList()
        val group = getGroup(doc) ?: return null
        val semester = getSemester(doc) ?: return null
        val resultDayList = mutableListOf<ScheduleDay>()
        for (day in days) {
            val dayChild = day.select("> *")
            val dayOfWeekText = dayChild[0].text()
            val dayOfWeek = getDayOfWeek(dayOfWeekText)
            val dayData = dayChild[1]
            val dateDataChild = dayData.select("> *")
            val resultLessonList = mutableListOf<Lesson>()
            for (timeWithLessons in dateDataChild) {
                val timeWithLessonsChild = timeWithLessons.select("> *")
                val lessonsTimeText = timeWithLessonsChild[0].text()
                val lessonTime = getLessonTime(lessonsTimeText)
                val lessons = timeWithLessonsChild[1]
                val lessonsChild = lessons.select("> *")
                for (lesson in lessonsChild) {
                    val newLesson = parseLesson(lesson, lessonTime)
                    newLesson?.let { resultLessonList.add(it) }
                }
            }
            val scheduleDay = ScheduleDay(dayOfWeek = dayOfWeek, lessons = resultLessonList)
            resultDayList.add(scheduleDay)
        }
        return Schedule(group = group, semester = semester, resultDayList)
    }

    private fun parseLesson(lesson: Element, lessonTime: LessonTime): Lesson? {
        val lessonName = getLessonName(lesson) ?: return null
        val lessonDateType = getLessonDateType(lesson)
        val uniqueWeeks =
            if (lessonDateType == LessonDateType.UNIQUE) getUniqueWeeks(lesson) else null
        val teacher = getTeacher(lesson)
        val cabinet = getCabinet(lesson)
        val lessonType = getLessonType(lesson)
        return Lesson(
            name = lessonName,
            dateType = lessonDateType,
            dateUniqueWeeks = uniqueWeeks,
            teacher = teacher,
            cabinet = cabinet,
            type = lessonType,
            time = lessonTime,
        )
    }

    private fun getLessonName(lesson: Element): String? {
        val lessonNameRaw =
            lesson.select(".schedule__table-item").firstOrNull()?.ownText() ?: return null
        val lessonName = lessonNameRaw.replace("·", "").trim()
        return lessonName.ifEmpty { null }
    }

    private fun getLessonDateType(lesson: Element): LessonDateType {
        val dateTypeText = lesson.select(".schedule__table-label").text()
        return when {
            dateTypeText.contains(LessonDateType.ODD.sourceName) -> LessonDateType.ODD
            dateTypeText.contains(LessonDateType.EVEN.sourceName) -> LessonDateType.EVEN
            dateTypeText.contains(LessonDateType.UNIQUE.sourceName) -> LessonDateType.UNIQUE
            else -> LessonDateType.ALL
        }
    }

    private fun getUniqueWeeks(lesson: Element): List<Int> {
        val dateTypeText = lesson.select(".schedule__table-label").text()
        val weeksList = dateTypeText.split(' ').toMutableList()
        weeksList.removeAt(0)
        return weeksList.map { week -> week.toInt() }
    }

    private fun getTeacher(lesson: Element): Teacher {
        val teacherFullInfo = lesson.select("a")
        val teacherName = teacherFullInfo.text().ifEmpty { null }
        val teacherUrl = teacherFullInfo.attr("href").ifEmpty { null }
        return Teacher(name = teacherName, url = teacherUrl)
    }

    private fun getCabinet(lesson: Element): String? {
        return lesson.select(".schedule__table-class").firstOrNull()?.text()?.ifEmpty { null }
    }

    private fun getLessonType(lesson: Element): LessonType {
        val lessonType = lesson.select(".schedule__table-typework").text()
        return when {
            lessonType.contains(LessonType.LABORATORY.sourceName) -> LessonType.LABORATORY
            lessonType.contains(LessonType.LECTURE.sourceName) -> LessonType.LECTURE
            lessonType.contains(LessonType.PRACTICE.sourceName) -> LessonType.PRACTICE
            else -> LessonType.OTHER
        }
    }

    private fun getGroup(doc: Document): String? {
        return doc.select(".schedule__title-h1").text().ifEmpty { null }
    }

    private fun getSemester(doc: Document): String? {
        return doc.select(".schedule__title-content").text().ifEmpty { null }
    }

    private fun getDayOfWeek(day: String): Int {
        return when (day) {
            "пн" -> 1
            "вт" -> 2
            "ср" -> 3
            "чт" -> 4
            "пт" -> 5
            "сб" -> 6
            else -> throw IllegalArgumentException("Unknown day of the week")
        }
    }

    private fun getLessonTime(lessonTimeText: String): LessonTime {
        val time = lessonTimeText.split('-')
        val startTime = time.first().split(':')
        val endTime = time.last().split(':')
        val localTimeStart = LocalTime.of(startTime.first().toInt(), startTime.last().toInt())
        val localTimeEnd = LocalTime.of(endTime.first().toInt(), endTime.last().toInt())
        return LessonTime(timeStart = localTimeStart, timeEnd = localTimeEnd)
    }
}