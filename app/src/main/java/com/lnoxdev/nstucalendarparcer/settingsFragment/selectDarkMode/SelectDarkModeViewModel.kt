package com.lnoxdev.nstucalendarparcer.settingsFragment.selectDarkMode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnoxdev.data.appSettings.SettingsManager
import com.lnoxdev.nstucalendarparcer.models.UiDarkMode
import com.lnoxdev.nstucalendarparcer.models.toAppDarkMode
import com.lnoxdev.nstucalendarparcer.models.toUiDarkMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectDarkModeViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {
    private val _selectedDarkMode = MutableStateFlow<UiDarkMode?>(null)
    val selectedDarkMode: StateFlow<UiDarkMode?> = _selectedDarkMode

    init {
        viewModelScope.launch {
            settingsManager.settings.collect {
                val darkMode = it.darkMode.toUiDarkMode()
                _selectedDarkMode.emit(darkMode)
            }
        }
    }

    fun changeDarkMode(darkMode: UiDarkMode){
        settingsManager.changeDarkMode(darkMode.toAppDarkMode())
    }

}