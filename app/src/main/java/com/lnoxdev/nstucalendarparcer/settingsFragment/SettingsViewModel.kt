package com.lnoxdev.nstucalendarparcer.settingsFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnoxdev.data.appSettings.SettingsManager
import com.lnoxdev.nstucalendarparcer.models.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val settingsManager: SettingsManager) :
    ViewModel() {
    private val _uiState = MutableStateFlow<SettingsUiState?>(null)
    val uiState: StateFlow<SettingsUiState?> = _uiState

    init {
        viewModelScope.launch {
            settingsManager.settings.collect {
                val newUiState = SettingsUiState(
                    group = it.group,
                    is12TimeFormat = it.is12TimeFormat
                )
                _uiState.value = newUiState
            }
        }
    }

    fun changeTimeFormat(is12TimeFormat: Boolean){
        settingsManager.changeTimeFormat(is12TimeFormat)
    }
}