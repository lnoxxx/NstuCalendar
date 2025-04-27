package com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders.themeRecyclerView

import androidx.recyclerview.widget.DiffUtil
import com.lnoxdev.nstucalendarparcer.models.UiAppTheme

class SettingsThemeDiffUtil(
    private val newTheme: UiAppTheme,
    private val oldTheme: UiAppTheme?,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return UiAppTheme.entries.size
    }

    override fun getNewListSize(): Int {
        return UiAppTheme.entries.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val themeItem = UiAppTheme.entries[newItemPosition]
        val oldSelected = themeItem == oldTheme
        val newSelected = themeItem == newTheme
        return oldSelected == newSelected
    }

}