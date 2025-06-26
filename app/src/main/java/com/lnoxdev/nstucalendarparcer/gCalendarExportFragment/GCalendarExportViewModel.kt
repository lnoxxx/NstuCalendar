package com.lnoxdev.nstucalendarparcer.gCalendarExportFragment

import androidx.lifecycle.ViewModel
import com.lnoxdev.data.CalendarAddingEventException
import com.lnoxdev.data.CalendarCreateException
import com.lnoxdev.data.CalendarDeleteException
import com.lnoxdev.data.CalendarPermissionRequired
import com.lnoxdev.data.GCalendarExportException
import com.lnoxdev.data.GetScheduleException
import com.lnoxdev.data.gCalendarExport.GCalendarExportManager
import com.lnoxdev.data.models.gCalendarExport.ExportSettings
import com.lnoxdev.nstucalendarparcer.models.UiCalendarExportException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GCalendarExportViewModel @Inject constructor(
    private val gCalendarExportManager: GCalendarExportManager
) : ViewModel() {

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow = _loadingFlow.asStateFlow()

    private val _finishExportFlow = MutableSharedFlow<Boolean>()
    val finishExportFlow = _finishExportFlow.asSharedFlow()

    private val _exceptionFlow: MutableSharedFlow<UiCalendarExportException?> = MutableSharedFlow()
    val exceptionFlow = _exceptionFlow.asSharedFlow()

    fun export(reminderTime: Int?) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _loadingFlow.emit(true)
                gCalendarExportManager.export(ExportSettings(reminderMinutes = reminderTime))
                _finishExportFlow.emit(true)
            } catch (e: GCalendarExportException) {
                val uiPermission = when (e) {
                    is CalendarAddingEventException -> UiCalendarExportException.ADD
                    is CalendarCreateException -> UiCalendarExportException.CREATE
                    is CalendarDeleteException -> UiCalendarExportException.DELETE
                    is CalendarPermissionRequired -> UiCalendarExportException.PERMISSION
                    is GetScheduleException -> UiCalendarExportException.SCHEDULE
                }
                _exceptionFlow.emit(uiPermission)
            } catch (e: Exception) {
                _exceptionFlow.emit(UiCalendarExportException.UNKNOWN)
            } finally {
                _loadingFlow.emit(false)
            }
        }
    }

    fun deleteCalendar() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _loadingFlow.emit(true)
                gCalendarExportManager.deleteCalendar()
            } catch (e: GCalendarExportException) {
                val uiPermission = when (e) {
                    is CalendarAddingEventException -> UiCalendarExportException.ADD
                    is CalendarCreateException -> UiCalendarExportException.CREATE
                    is CalendarDeleteException -> UiCalendarExportException.DELETE
                    is CalendarPermissionRequired -> UiCalendarExportException.PERMISSION
                    is GetScheduleException -> UiCalendarExportException.SCHEDULE
                }
                _exceptionFlow.emit(uiPermission)
            } catch (e: Exception) {
                _exceptionFlow.emit(UiCalendarExportException.UNKNOWN)
            } finally {
                _loadingFlow.emit(false)
            }
        }
    }
}