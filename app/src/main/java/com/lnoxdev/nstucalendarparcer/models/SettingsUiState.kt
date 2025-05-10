package com.lnoxdev.nstucalendarparcer.models

import android.os.Parcelable

data class SettingsUiState(
    val group: String?,
    val is12TimeFormat: Boolean,
    val monet: Boolean,
    val appTheme: UiAppTheme,
    val themeRvState: Parcelable?,
    val language: UiAppLanguage,
    val darkMode: UiDarkMode
)