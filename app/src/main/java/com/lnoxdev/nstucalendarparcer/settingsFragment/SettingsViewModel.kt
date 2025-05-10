package com.lnoxdev.nstucalendarparcer.settingsFragment

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnoxdev.data.appSettings.SettingsManager
import com.lnoxdev.nstucalendarparcer.models.SettingsUiState
import com.lnoxdev.nstucalendarparcer.models.UiAppTheme
import com.lnoxdev.nstucalendarparcer.models.toAppTheme
import com.lnoxdev.nstucalendarparcer.models.toUiAppLanguage
import com.lnoxdev.nstucalendarparcer.models.toUiAppTheme
import com.lnoxdev.nstucalendarparcer.models.toUiDarkMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {
    private val _uiState = MutableStateFlow<SettingsUiState?>(null)
    val uiState: StateFlow<SettingsUiState?> = _uiState

    init {
        viewModelScope.launch {
            settingsManager.settings.collect {
                val newUiState = SettingsUiState(
                    group = it.group,
                    is12TimeFormat = it.is12TimeFormat,
                    monet = it.monetTheme,
                    appTheme = it.appTheme.toUiAppTheme(),
                    themeRvState = themeRvState,
                    language = it.language.toUiAppLanguage(),
                    darkMode = it.darkMode.toUiDarkMode(),
                )
                _uiState.value = newUiState
            }
        }
    }

    fun changeTimeFormat(is12TimeFormat: Boolean) {
        settingsManager.changeTimeFormat(is12TimeFormat)
    }

    suspend fun changeMonetTheme(enable: Boolean): Boolean {
        return settingsManager.changeMonetThemeAsyncWithResult(enable)
    }

    private var themeRvState: Parcelable? = null
    suspend fun changeTheme(theme: UiAppTheme, newThemeRvState: Parcelable?): Boolean {
        themeRvState = newThemeRvState
        return settingsManager.changeAppThemeAsyncWithResult(theme.toAppTheme())
    }
}