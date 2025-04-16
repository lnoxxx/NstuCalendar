package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnoxdev.data.DataException
import com.lnoxdev.data.InternetException
import com.lnoxdev.data.ParseException
import com.lnoxdev.data.SaveException
import com.lnoxdev.data.SettingGroupException
import com.lnoxdev.data.Time
import com.lnoxdev.data.appSettings.SettingsManager
import com.lnoxdev.data.neti.NetiScheduleRepository
import com.lnoxdev.nstucalendarparcer.models.UiExceptions
import com.lnoxdev.nstucalendarparcer.models.WeeklyScheduleState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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

    private val _exception = MutableStateFlow<UiExceptions?>(null)
    val exception: StateFlow<UiExceptions?> = _exception

    init {
        viewModelScope.launch { settingsManager.settings.collect { startUpdateSchedule() } }
        viewModelScope.launch {
            scheduleRepository.weeklySchedule.collect { schedule ->
                val group = settingsManager.settings.first().group
                val is12hourTimeFormat = settingsManager.settings.first().is12TimeFormat
                val nowDate = Time.getNowDateTime().toLocalDate()
                schedule?.let {
                    _uiState.value = WeeklyScheduleState(
                        lastUpdateTime = it.saveTime,
                        weeksCount = it.weeks.size,
                        group = group,
                        nowWeekIndex = getWeekNumber(schedule.startDate, schedule.endDate, nowDate),
                        is12HourTimeFormat = is12hourTimeFormat
                    )
                }
            }
        }
    }

    fun startUpdateSchedule() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _isUpdate.value = true
                scheduleRepository.updateSchedule()
            } catch (e: DataException) {
                when (e) {
                    is InternetException -> commitException(UiExceptions.INTERNET)
                    is ParseException -> commitException(UiExceptions.PARSE)
                    is SaveException -> commitException(UiExceptions.SAVE)
                    is SettingGroupException -> commitException(UiExceptions.SETTING_GROUP)
                }
            } catch (e: Exception) {
                commitException(UiExceptions.UNKNOWN)
            } finally {
                _isUpdate.value = false
            }
        }
    }

    private suspend fun commitException(exception: UiExceptions) {
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
        return (daysSinceStart / 7).toInt()
    }

}