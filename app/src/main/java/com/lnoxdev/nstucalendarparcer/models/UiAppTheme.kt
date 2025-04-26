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
    MOUNTAIN_MEADOW(
        R.string.theme_mountain_meadow,
        R.color.mountain_meadow_primary,
        R.color.mountain_meadow_secondaryContainer,
        R.color.mountain_meadow_onTertiaryContainer,
        R.style.NstuCalendar_Theme_MountainMeadow
    ),
    PEAR(
        R.string.theme_pear,
        R.color.pear_primary,
        R.color.pear_secondaryContainer,
        R.color.pear_onTertiaryContainer,
        R.style.NstuCalendar_Theme_Pear
    ),
    SALMON_ORANGE(
        R.string.theme_salmon_orange,
        R.color.salmon_orange_primary,
        R.color.salmon_orange_secondaryContainer,
        R.color.salmon_orange_onTertiaryContainer,
        R.style.NstuCalendar_Theme_SalmonOrange
    ),
    TELEMAGENTA(
        R.string.theme_telemagenta,
        R.color.telemagenta_primary,
        R.color.telemagenta_secondaryContainer,
        R.color.telemagenta_onTertiaryContainer,
        R.style.NstuCalendar_Theme_Telemagenta
    ),
    TOAD_IN_LOVE(
        R.string.theme_toad_in_love,
        R.color.toad_in_love_primary,
        R.color.toad_in_love_secondaryContainer,
        R.color.toad_in_love_onTertiaryContainer,
        R.style.NstuCalendar_Theme_ToadInLove
    ),
    TURQUOISE(
        R.string.theme_turquoise,
        R.color.turquoise_primary,
        R.color.turquoise_secondaryContainer,
        R.color.turquoise_onTertiaryContainer,
        R.style.NstuCalendar_Theme_Turquoise
    ),
}

fun UiAppTheme.toAppTheme(): SettingsManager.Companion.AppTheme {
    return when (this) {
        UiAppTheme.NSTU -> SettingsManager.Companion.AppTheme.NSTU
        UiAppTheme.CORNFLOWER -> SettingsManager.Companion.AppTheme.CORNFLOWER
        UiAppTheme.DARK_ORCHID -> SettingsManager.Companion.AppTheme.DARK_ORCHID
        UiAppTheme.MOUNTAIN_MEADOW -> SettingsManager.Companion.AppTheme.MOUNTAIN_MEADOW
        UiAppTheme.PEAR -> SettingsManager.Companion.AppTheme.PEAR
        UiAppTheme.SALMON_ORANGE -> SettingsManager.Companion.AppTheme.SALMON_ORANGE
        UiAppTheme.TELEMAGENTA -> SettingsManager.Companion.AppTheme.TELEMAGENTA
        UiAppTheme.TOAD_IN_LOVE -> SettingsManager.Companion.AppTheme.TOAD_IN_LOVE
        UiAppTheme.TURQUOISE -> SettingsManager.Companion.AppTheme.TURQUOISE
    }
}

fun SettingsManager.Companion.AppTheme.toUiAppTheme(): UiAppTheme {
    return when (this) {
        SettingsManager.Companion.AppTheme.NSTU -> UiAppTheme.NSTU
        SettingsManager.Companion.AppTheme.CORNFLOWER -> UiAppTheme.CORNFLOWER
        SettingsManager.Companion.AppTheme.DARK_ORCHID -> UiAppTheme.DARK_ORCHID
        SettingsManager.Companion.AppTheme.MOUNTAIN_MEADOW -> UiAppTheme.MOUNTAIN_MEADOW
        SettingsManager.Companion.AppTheme.PEAR -> UiAppTheme.PEAR
        SettingsManager.Companion.AppTheme.SALMON_ORANGE -> UiAppTheme.SALMON_ORANGE
        SettingsManager.Companion.AppTheme.TELEMAGENTA -> UiAppTheme.TELEMAGENTA
        SettingsManager.Companion.AppTheme.TOAD_IN_LOVE -> UiAppTheme.TOAD_IN_LOVE
        SettingsManager.Companion.AppTheme.TURQUOISE -> UiAppTheme.TURQUOISE
    }
}