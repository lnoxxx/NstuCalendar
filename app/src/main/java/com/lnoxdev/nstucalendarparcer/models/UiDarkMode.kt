package com.lnoxdev.nstucalendarparcer.models

import androidx.annotation.StringRes
import com.lnoxdev.data.appSettings.SettingsManager
import com.lnoxdev.nstucalendarparcer.R

enum class UiDarkMode(@StringRes val nameResId: Int) {
    LIGHT(R.string.settings_light),
    DARK(R.string.settings_dark),
    SYSTEM(R.string.settings_as_on_the_device)
}

fun UiDarkMode.toAppDarkMode(): SettingsManager.Companion.AppDarkMode {
    return when (this) {
        UiDarkMode.LIGHT -> SettingsManager.Companion.AppDarkMode.LIGHT
        UiDarkMode.DARK -> SettingsManager.Companion.AppDarkMode.DARK
        UiDarkMode.SYSTEM -> SettingsManager.Companion.AppDarkMode.SYSTEM
    }
}

fun SettingsManager.Companion.AppDarkMode.toUiDarkMode(): UiDarkMode {
    return when (this) {
        SettingsManager.Companion.AppDarkMode.LIGHT -> UiDarkMode.LIGHT
        SettingsManager.Companion.AppDarkMode.DARK -> UiDarkMode.DARK
        SettingsManager.Companion.AppDarkMode.SYSTEM -> UiDarkMode.SYSTEM
    }
}