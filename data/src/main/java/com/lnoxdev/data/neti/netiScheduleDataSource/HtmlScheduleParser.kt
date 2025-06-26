package com.lnoxdev.data.neti.netiScheduleDataSource

import com.lnoxdev.data.Time
import com.lnoxdev.data.models.schedule.ScheduleDay
import com.lnoxdev.data.models.schedule.lesson.Lesson
import com.lnoxdev.data.models.schedule.lesson.LessonDateType
import com.lnoxdev.data.models.schedule.lesson.LessonType
import com.lnoxdev.data.models.schedule.Teacher
import com.lnoxdev.data.models.schedule.lesson.LessonTime
import com.lnoxdev.data.neti.netiScheduleDatabase.Schedule
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.time.LocalDate
import java.time.LocalTime

data class WeeksParseResult(val startDay: Int, val startMonth: Int, val weekCount: Int)

object HtmlScheduleParser {

    fun parseSchedule(stringHtml: String, startDate: LocalDate, endDate: LocalDate): Schedule? {
        val doc = Jsoup.parse(stringHtml)
        val group = parseGroup(doc) ?: return null
        val semester = parseSemester(doc) ?: return null
        val days = doc.select(".schedule__table-row[data-empty]").toList()
        val dayList = parseDaysToList(days)
        return Schedule(
            group = group,
            semester = semester,
            days = dayList,
            saveTime = Time.getNowDateTime(),
            startDate = startDate,
            endDate = endDate
        )
    }

    private fun parseDaysToList(days: List<Element>): MutableList<ScheduleDay> {
        val resultDayList = mutableListOf<ScheduleDay>()
        for (day in days) {
            val dayChild = day.select("> *")
            val dayOfWeekText = dayChild[0].text()
            val dayOfWeek = parseDayOfWeek(dayOfWeekText) ?: continue
            val dayData = dayChild[1]
            val dateDataChild = dayData.select("> *")
            val lessonList = parseLessonsToList(dateDataChild)
            val scheduleDay = ScheduleDay(dayOfWeek = dayOfWeek, lessons = lessonList)
            resultDayList.add(scheduleDay)
        }
        return resultDayList
    }

    private fun parseLessonsToList(dateDataChild: List<Element>): MutableList<Lesson> {
        val resultLessonList = mutableListOf<Lesson>()
        for (timeWithLessons in dateDataChild) {
            val timeWithLessonsChild = timeWithLessons.select("> *")
            val lessonsTimeText = timeWithLessonsChild[0].text()
            val lessonTime = parseLessonTime(lessonsTimeText)
            val lessons = timeWithLessonsChild[1]
            val lessonsChild = lessons.select("> *")
            for (lesson in lessonsChild) {
                val newLesson = parseLesson(lesson, lessonTime)
                newLesson?.let { resultLessonList.add(it) }
            }
        }
        return resultLessonList
    }

    private fun parseLesson(lesson: Element, lessonTime: LessonTime): Lesson? {
        val lessonName = parseLessonName(lesson) ?: return null
        val lessonDateType = parseLessonDateType(lesson)
        val uniqueWeeks =
            if (lessonDateType == LessonDateType.UNIQUE) parseUniqueWeeks(lesson) else null
        val teacher = parseTeacher(lesson)
        val cabinet = parseCabinet(lesson)
        val lessonType = parseLessonType(lesson)
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

    private fun parseLessonName(lesson: Element): String? {
        val lessonNameRaw =
            lesson.select(".schedule__table-item").firstOrNull()?.ownText() ?: return null
        val lessonName = lessonNameRaw
            .replace("·", "")
            .replace(",", "")
            .trim()
        return lessonName.ifEmpty { null }
    }

    private fun parseLessonDateType(lesson: Element): LessonDateType {
        val dateTypeText = lesson.select(".schedule__table-label").text()
        return when {
            dateTypeText.contains("по нечётным") -> LessonDateType.ODD
            dateTypeText.contains("по чётным") -> LessonDateType.EVEN
            dateTypeText.contains("недели") -> LessonDateType.UNIQUE
            else -> LessonDateType.ALL
        }
    }

    private fun parseUniqueWeeks(lesson: Element): List<Int> {
        val dateTypeText = lesson.select(".schedule__table-label").text()
        val weeksList = dateTypeText.split(' ').toMutableList()
        weeksList.removeAt(0)
        return weeksList.map { week -> week.toInt() }
    }

    private fun parseTeacher(lesson: Element): Teacher? {
        val teacherFullInfo = lesson.select("a")
        val teacherName = teacherFullInfo.text().ifEmpty { null }
        val teacherUrl = teacherFullInfo.attr("href").ifEmpty { null }
        if (teacherName == null || teacherUrl == null) return null
        return Teacher(name = teacherName, url = teacherUrl)
    }

    private fun parseCabinet(lesson: Element): String? {
        return lesson.select(".schedule__table-class").firstOrNull()?.text()?.ifEmpty { null }
    }

    private fun parseLessonType(lesson: Element): LessonType {
        val lessonType = lesson.select(".schedule__table-typework").text()
        return when {
            lessonType.contains("Лабораторная") -> LessonType.LABORATORY
            lessonType.contains("Лекция") -> LessonType.LECTURE
            lessonType.contains("Практика") -> LessonType.PRACTICE
            else -> LessonType.OTHER
        }
    }

    private fun parseGroup(doc: Document): String? {
        return doc.select(".schedule__title-h1").text().ifEmpty { null }
    }

    private fun parseSemester(doc: Document): String? {
        return doc.select(".schedule__title-content").text().ifEmpty { null }
    }

    private fun parseDayOfWeek(day: String): Int? {
        return when (day) {
            "пн" -> 1
            "вт" -> 2
            "ср" -> 3
            "чт" -> 4
            "пт" -> 5
            "сб" -> 6
            else -> null
        }
    }

    private fun parseLessonTime(lessonTimeText: String): LessonTime {
        val time = lessonTimeText.split('-')
        val startTime = time.first().split(':')
        val endTime = time.last().split(':')
        val localTimeStart = LocalTime.of(startTime.first().toInt(), startTime.last().toInt())
        val localTimeEnd = LocalTime.of(endTime.first().toInt(), endTime.last().toInt())
        return LessonTime(timeStart = localTimeStart, timeEnd = localTimeEnd)
    }

    fun parseScheduleWeek(stringHtml: String): WeeksParseResult {
        val doc = Jsoup.parse(stringHtml)
        val firstDay = doc.select(".schedule__table-day").firstOrNull() ?: throw Exception()
        val firstDayDate = firstDay.select(".schedule__table-date").text()
        val startDay = firstDayDate.split('.')[0].toIntOrNull() ?: throw Exception()
        val startMonth = firstDayDate.split('.')[1].toIntOrNull() ?: throw Exception()
        val lastWeek = doc.select("a.schedule__weeks-item.link-dashed.js-week-item").lastOrNull()
        val weekCount = lastWeek?.attr("data-week")?.toIntOrNull() ?: throw Exception()
        return WeeksParseResult(startDay, startMonth, weekCount)
    }
}