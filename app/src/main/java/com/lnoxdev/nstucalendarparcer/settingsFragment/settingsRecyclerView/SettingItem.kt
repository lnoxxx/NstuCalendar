package com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView

import com.lnoxdev.nstucalendarparcer.R

enum class SettingsItem(val titleResId: Int, val iconResId: Int) {
    GROUP(R.string.settings_group, R.drawable.ic_settings_group),
    LANGUAGE(R.string.settings_language, R.drawable.ic_settings_language),
    TIME_FORMAT(R.string.settings_time, R.drawable.ic_settings_time_format),
    THEME(R.string.settings_theme, R.drawable.ic_settings_theme),
    MONET(R.string.settings_monet, R.drawable.ic_settings_monet),
    DARK_MODE(R.string.settings_dark_mode, R.drawable.ic_settings_dark_mode),
}