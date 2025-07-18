package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnoxdev.data.ScheduleException
import com.lnoxdev.data.InternetException
import com.lnoxdev.data.ParseException
import com.lnoxdev.data.SaveException
import com.lnoxdev.data.SettingGroupException
import com.lnoxdev.data.Time
import com.lnoxdev.data.appSettings.SettingsManager
import com.lnoxdev.data.neti.NetiScheduleRepository
import com.lnoxdev.nstucalendarparcer.models.UiScheduleExceptions
import com.lnoxdev.nstucalendarparcer.models.WeeklyScheduleState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class WeeklyScheduleViewModel @Inject constructor(
    private val scheduleRepository: NetiScheduleRepository,
    private val settingsManager: SettingsManager,
) : ViewModel() {

    private val _uiState: MutableStateFlow<WeeklyScheduleState?> = MutableStateFlow(null)
    val uiState: StateFlow<WeeklyScheduleState?> = _uiState

    private val _isUpdate = MutableStateFlow(false)
    val isUpdate: StateFlow<Boolean> = _isUpdate

    private val _exception = MutableStateFlow<UiScheduleExceptions?>(null)
    val exception: StateFlow<UiScheduleExceptions?> = _exception

    init {
        viewModelScope.launch {
            scheduleRepository.weeklySchedule.combine(settingsManager.settings) { schedule, settings ->
                val group = settings.group
                val is12hourTimeFormat = settings.is12TimeFormat
                val nowDate = Time.getNowDateTime().toLocalDate()
                WeeklyScheduleState(
                        lastUpdateTime = schedule?.saveTime,
                        weeksCount = schedule?.weeks?.size,
                        group = group,
                        nowWeekIndex = getWeekNumber(schedule?.startDate, schedule?.endDate, nowDate),
                        is12HourTimeFormat = is12hourTimeFormat
                )
            }.collectLatest { newUiState ->
                _uiState.value = newUiState
            }
        }
        viewModelScope.launch { settingsManager.settings.collectLatest { startUpdateSchedule() } }
    }

    fun startUpdateSchedule() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _isUpdate.emit(true)
                scheduleRepository.updateSchedule()
                _exception.emit(null)
            } catch (e: ScheduleException) {
                when (e) {
                    is InternetException -> emitException(UiScheduleExceptions.INTERNET)
                    is ParseException -> emitException(UiScheduleExceptions.PARSE)
                    is SaveException -> emitException(UiScheduleExceptions.SAVE)
                    is SettingGroupException -> emitException(UiScheduleExceptions.SETTING_GROUP)
                }
            } catch (e: Exception) {
                emitException(UiScheduleExceptions.UNKNOWN)
            } finally {
                _isUpdate.emit(false)
            }
        }
    }

    private suspend fun emitException(exception: UiScheduleExceptions) {
        _exception.emit(exception)
    }

    private fun getWeekNumber(
        startDate: LocalDate?,
        endDate: LocalDate?,
        targetDate: LocalDate
    ): Int? {
        startDate ?: return null
        endDate ?: return null
        if (targetDate < startDate || targetDate > endDate) {
            return null
        }
        val daysSinceStart = ChronoUnit.DAYS.between(startDate, targetDate)
        val dayOfWeek = daysSinceStart.mod(7)
        val sundayTransfer = if (dayOfWeek == 6) 1 else 0
        return (daysSinceStart / 7).toInt() + sundayTransfer
    }

}