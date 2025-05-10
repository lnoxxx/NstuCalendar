package com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView

import androidx.recyclerview.widget.DiffUtil
import com.lnoxdev.nstucalendarparcer.models.SettingsUiState

class SettingsRecyclerViewDiffUtil(
    private val oldSettings: SettingsUiState?,
    private val newSettings: SettingsUiState,
) : DiffUtil.Callback() {

    private val settingsItemsList = SettingsItem.entries

    override fun getOldListSize(): Int {
        return settingsItemsList.size
    }

    override fun getNewListSize(): Int {
        return settingsItemsList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldSettings == null) return false
        val item = settingsItemsList[newItemPosition]
        return when (item) {
            SettingsItem.GROUP -> oldSettings.group == newSettings.group
            SettingsItem.TIME_FORMAT -> oldSettings.is12TimeFormat == newSettings.is12TimeFormat
            SettingsItem.MONET -> oldSettings.monet == newSettings.monet
            SettingsItem.THEME -> oldSettings.appTheme == newSettings.appTheme
            SettingsItem.LANGUAGE -> oldSettings.language == newSettings.language
            SettingsItem.DARK_MODE -> oldSettings.darkMode == newSettings.darkMode
        }
    }

}