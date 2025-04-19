package com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView

import com.lnoxdev.nstucalendarparcer.R

enum class SettingsItem(val titleResId: Int, val iconResId: Int) {
    GROUP(R.string.settings_group, R.drawable.ic_settings_group),
    TIME_FORMAT(R.string.settings_time, R.drawable.ic_settings_time_format),
    MONET(R.string.settings_monet, R.drawable.ic_settings_monet),
    THEME(R.string.settings_theme, R.drawable.ic_settings_theme)
}