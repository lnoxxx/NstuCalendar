package com.lnoxdev.data.models

import com.lnoxdev.data.appSettings.SettingsManager

data class Settings(
    val group: String?,
    val is12TimeFormat: Boolean,
    val monetTheme: Boolean,
    val appTheme: SettingsManager.Companion.AppTheme
)
