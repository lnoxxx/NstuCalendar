package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnoxdev.data.Time
import com.lnoxdev.data.models.schedule.lesson.LessonType
import com.lnoxdev.data.models.weeklySchedule.ScheduleWeek
import com.lnoxdev.data.netiSchedule.NetiScheduleRepository
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleDate
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleItem
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleLesson
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleLessonType
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleNowLessonTime
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleTeacher
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class WeekViewModel @Inject constructor(private val netiScheduleRepository: NetiScheduleRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow<WeekScheduleUiState?>(null)
    val uiState: StateFlow<WeekScheduleUiState?> = _uiState

    private var weekIndex: Int? = null

    fun setWeekIndex(index: Int) {
        weekIndex = index
        startCollect(index)
    }

    private fun startCollect(weekIndex: Int) {
        viewModelScope.launch {
            val scheduleFlow: Flow<MutableList<WeekScheduleItem>?> =
                netiScheduleRepository.getWeekSchedule(weekIndex)
                    .combine(Time.getCurrentDateTimeFlow()) { schedule, dateTime ->
                        schedule?.let {
                            val scheduleList = mutableListOf<WeekScheduleItem>()
                            for (day in schedule.days) {
                                val dayDate = day.date
                                    ?: throw IllegalStateException("WeekViewModel not found date of lessons")
                                val weekScheduleDate = WeekScheduleDate(
                                    date = dayDate,
                                    dayOfWeek = dayDate.dayOfWeek.value,
                                    isFinished = dayDate.isBefore(dateTime.toLocalDate())
                                )
                                scheduleList.add(weekScheduleDate)
                                for (lesson in day.lessons) {
                                    val localDateTimeOfStartLesson =
                                        LocalDateTime.of(dayDate, lesson.time.timeStart)
                                    val localDateTimeOfEndLesson =
                                        LocalDateTime.of(dayDate, lesson.time.timeEnd)
                                    val newLesson = WeekScheduleLesson(
                                        name = lesson.name,
                                        teacher = WeekScheduleTeacher(
                                            name = lesson.teacher?.name,
                                            url = lesson.teacher?.url
                                        ),
                                        cabinet = lesson.cabinet,
                                        type = when (lesson.type) {
                                            LessonType.LECTURE -> WeekScheduleLessonType.LECTURE
                                            LessonType.PRACTICE -> WeekScheduleLessonType.PRACTICE
                                            LessonType.LABORATORY -> WeekScheduleLessonType.LABORATORY
                                            LessonType.OTHER -> WeekScheduleLessonType.OTHER
                                        },
                                        startTime = lesson.time.timeStart,
                                        endTime = lesson.time.timeEnd,
                                        isFinished = dateTime.isAfter(localDateTimeOfEndLesson)
                                    )
                                    scheduleList.add(newLesson)
                                    val isNowLesson = dateTime.isAfter(localDateTimeOfStartLesson)
                                            && dateTime.isBefore(localDateTimeOfEndLesson)
                                    if (isNowLesson) {
                                        val totalTime = ChronoUnit.NANOS.between(
                                            lesson.time.timeStart,
                                            lesson.time.timeEnd
                                        )
                                        val elapsedTime = ChronoUnit.NANOS.between(
                                            lesson.time.timeStart,
                                            dateTime.toLocalTime()
                                        )
                                        val progress = elapsedTime.toDouble() / totalTime * 100

                                        val nowTime = WeekScheduleNowLessonTime(
                                            timeStart = lesson.time.timeStart,
                                            timeEnd = lesson.time.timeEnd,
                                            progress = progress.toInt()
                                        )
                                        scheduleList.add(nowTime)
                                    }
                                }
                            }
                            scheduleList
                        }
                    }
            scheduleFlow.collect {
                Log.d("testlog", it.toString())
            }
        }
    }

    private fun weekScheduleProcessing(week: ScheduleWeek) {
        val weekIndex =
            weekIndex ?: throw IllegalStateException("Set week index use setWeekIndex method!")

    }

    private fun emitNewUiState(uiState: WeekScheduleUiState) {
        _uiState.value = uiState
    }
}