package com.lnoxdev.nstucalendarparcer.settingsFragment.selectLanguage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnoxdev.data.appSettings.SettingsManager
import com.lnoxdev.nstucalendarparcer.models.UiAppLanguage
import com.lnoxdev.nstucalendarparcer.models.toAppLanguage
import com.lnoxdev.nstucalendarparcer.models.toUiAppLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectLanguageViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {
    private val _selectedLanguage = MutableStateFlow<UiAppLanguage?>(null)
    val selectedLanguage: StateFlow<UiAppLanguage?> = _selectedLanguage

    init {
        viewModelScope.launch {
            settingsManager.settings.collect {
                val lang = it.language.toUiAppLanguage()
                _selectedLanguage.emit(lang)
            }
        }
    }

    fun changeLang(lang: UiAppLanguage){
        settingsManager.changeLanguage(lang.toAppLanguage())
    }

}