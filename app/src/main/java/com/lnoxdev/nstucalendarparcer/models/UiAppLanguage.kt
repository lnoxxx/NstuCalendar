package com.lnoxdev.nstucalendarparcer.models

import androidx.annotation.StringRes
import com.lnoxdev.data.appSettings.SettingsManager
import com.lnoxdev.nstucalendarparcer.R

enum class UiAppLanguage(@StringRes val nameResId: Int) {
    EN(R.string.settings_english),
    RU(R.string.settings_russian),
    SYSTEM(R.string.settings_as_on_the_device)
}

fun SettingsManager.Companion.AppLanguage.toUiAppLanguage(): UiAppLanguage {
    return when (this) {
        SettingsManager.Companion.AppLanguage.EN -> UiAppLanguage.EN
        SettingsManager.Companion.AppLanguage.RU -> UiAppLanguage.RU
        SettingsManager.Companion.AppLanguage.SYSTEM -> UiAppLanguage.SYSTEM
    }
}

fun UiAppLanguage.toAppLanguage(): SettingsManager.Companion.AppLanguage {
    return when (this) {
        UiAppLanguage.EN -> SettingsManager.Companion.AppLanguage.EN
        UiAppLanguage.RU -> SettingsManager.Companion.AppLanguage.RU
        UiAppLanguage.SYSTEM -> SettingsManager.Companion.AppLanguage.SYSTEM
    }
}