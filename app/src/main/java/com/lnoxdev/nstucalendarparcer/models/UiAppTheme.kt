package com.lnoxdev.nstucalendarparcer.models

import com.lnoxdev.data.appSettings.SettingsManager
import com.lnoxdev.nstucalendarparcer.R

enum class UiAppTheme(
    val themeNameResId: Int,
    val colorResPrimary: Int,
    val colorResSecondary: Int,
    val colorResTertiary: Int,
    val themeRes: Int
) {
    NSTU(
        R.string.theme_nstu,
        R.color.md_theme_primary,
        R.color.md_theme_secondaryContainer,
        R.color.md_theme_onTertiaryContainer,
        R.style.NstuCalendar_Theme_Default
    ),
    CORNFLOWER(
        R.string.theme_cornflower,
        R.color.cornflower_primary,
        R.color.cornflower_secondaryContainer,
        R.color.cornflower_onTertiaryContainer,
        R.style.NstuCalendar_Theme_Cornflower
    ),
    DARK_ORCHID(
        R.string.theme_dark_orchid,
        R.color.dark_orchid_primary,
        R.color.dark_orchid_secondaryContainer,
        R.color.dark_orchid_onTertiaryContainer,
        R.style.NstuCalendar_Theme_DarkOrchid
    ),
}

fun UiAppTheme.toAppTheme(): SettingsManager.Companion.AppTheme {
    return when (this) {
        UiAppTheme.NSTU -> SettingsManager.Companion.AppTheme.NSTU
        UiAppTheme.CORNFLOWER -> SettingsManager.Companion.AppTheme.CORNFLOWER
        UiAppTheme.DARK_ORCHID -> SettingsManager.Companion.AppTheme.DARK_ORCHID
    }
}

fun SettingsManager.Companion.AppTheme.toUiAppTheme(): UiAppTheme {
    return when (this) {
        SettingsManager.Companion.AppTheme.NSTU -> UiAppTheme.NSTU
        SettingsManager.Companion.AppTheme.CORNFLOWER -> UiAppTheme.CORNFLOWER
        SettingsManager.Companion.AppTheme.DARK_ORCHID -> UiAppTheme.DARK_ORCHID
    }
}