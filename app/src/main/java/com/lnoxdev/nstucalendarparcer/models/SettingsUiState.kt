package com.lnoxdev.nstucalendarparcer.models

data class SettingsUiState(
    val group: String?,
    val is12TimeFormat: Boolean,
    val monet: Boolean,
    val appTheme: UiAppTheme,
)