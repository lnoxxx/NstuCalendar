package com.lnoxdev.nstucalendarparcer.gCalendarExportFragment

import androidx.lifecycle.ViewModel
import com.lnoxdev.data.gCalendarExport.GCalendarExportManager
import com.lnoxdev.data.models.gCalendarExport.ExportSettings
import com.lnoxdev.nstucalendarparcer.models.CalendarItem
import com.lnoxdev.nstucalendarparcer.models.GCalendarUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// in development!

@HiltViewModel
class GCalendarExportViewModel @Inject constructor(
    private val gCalendarExportManager: GCalendarExportManager
) : ViewModel() {
    private val _uiState: MutableStateFlow<GCalendarUiState?> = MutableStateFlow(getDefaultState())
    val uiState: StateFlow<GCalendarUiState?> = _uiState

    fun export(time: String) {
        val selectedCalendar =
            _uiState.value?.calendars?.find { it.isSelected }?.id ?: return
        val reminderTime = if (time.isEmpty()) null else time.toIntOrNull()
        CoroutineScope(Dispatchers.IO).launch {
            gCalendarExportManager.export(ExportSettings(selectedCalendar, reminderTime))
        }
    }

    fun changeSelectedCalendar(newCalendar: CalendarItem) {
        val oldUiState = _uiState.value ?: return
        val newList = oldUiState.calendars.map { it.copy(isSelected = it.id == newCalendar.id) }
        val newUiState = oldUiState.copy(calendars = newList)
        _uiState.value = newUiState
    }

    private fun getDefaultState(): GCalendarUiState? {
        return try {
            val calendarList = gCalendarExportManager.getCalendarList().toList()
                .map { CalendarItem(it.second, it.first, false) }
            GCalendarUiState(calendarList)
        } catch (e: Exception) {
            null
        }
    }
}