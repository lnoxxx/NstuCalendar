package com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lnoxdev.nstucalendarparcer.models.SettingsUiState
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.SettingsItem
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.SettingsRecyclerViewAdapter

abstract class SettingsItemViewHolder(
    view: View,
    protected val listener: SettingsRecyclerViewAdapter.SettingsRecyclerViewListener
) : RecyclerView.ViewHolder(view) {
    abstract fun bind(settings: SettingsUiState, settingsItem: SettingsItem)
}