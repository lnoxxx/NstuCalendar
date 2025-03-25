package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnoxdev.data.DataException
import com.lnoxdev.data.InternetException
import com.lnoxdev.data.ParseException
import com.lnoxdev.data.SaveException
import com.lnoxdev.data.SettingGroupException
import com.lnoxdev.data.netiSchedule.NetiScheduleRepository
import com.lnoxdev.nstucalendarparcer.models.UiWeeklyScheduleExceptions
import com.lnoxdev.nstucalendarparcer.models.WeeklyScheduleState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeeklyScheduleViewModel @Inject constructor(
    private val scheduleRepository: NetiScheduleRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<WeeklyScheduleState?> = MutableStateFlow(null)
    val uiState: StateFlow<WeeklyScheduleState?> = _uiState

    init {
        startUpdateSchedule()
        startCollectSchedule()
    }

    private fun startCollectSchedule() {
        viewModelScope.launch {
            scheduleRepository.weeklySchedule.collect { schedule ->
                schedule?.let {
                    _uiState.value = WeeklyScheduleState(
                        lastUpdateTime = it.saveTime,
                        weeksCount = it.weeks.size,
                    )
                }
            }
        }
    }

    private fun startUpdateSchedule() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                commitUpdateStatus(true)
                scheduleRepository.updateSchedule()
            } catch (e: DataException) {
                when (e) {
                    is InternetException -> commitException(UiWeeklyScheduleExceptions.INTERNET)
                    is ParseException -> commitException(UiWeeklyScheduleExceptions.PARSE)
                    is SaveException -> commitException(UiWeeklyScheduleExceptions.SAVE)
                    is SettingGroupException -> commitException(UiWeeklyScheduleExceptions.SETTING_GROUP)
                }
            } catch (e: Exception) {
                commitException(UiWeeklyScheduleExceptions.UNKNOWN)
            } finally {
                commitUpdateStatus(false)
            }
        }
    }

    private fun commitException(exception: UiWeeklyScheduleExceptions) {
        _uiState.value = uiState.value?.copy(exception = exception)
    }

    private fun commitUpdateStatus(isUpdate: Boolean) {
        _uiState.value = uiState.value?.copy(isUpdate = isUpdate)
    }
}