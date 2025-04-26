package com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders

import android.view.View
import com.lnoxdev.nstucalendarparcer.databinding.ItemSettingsSwitchBinding
import com.lnoxdev.nstucalendarparcer.models.SettingsUiState
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.SettingsItem
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.SettingsRecyclerViewAdapter

class SettingsSwitchViewHolder(
    view: View,
    listener: SettingsRecyclerViewAdapter.SettingsRecyclerViewListener
) : SettingsItemViewHolder(view, listener) {
    private val binding = ItemSettingsSwitchBinding.bind(view)
    override fun bind(settings: SettingsUiState, settingsItem: SettingsItem) {
        binding.tvTitle.text = itemView.context.getString(settingsItem.titleResId)
        binding.tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(
            settingsItem.iconResId,
            0,
            0,
            0
        )
        if (settingsItem == SettingsItem.TIME_FORMAT) {
            binding.swc.isChecked = settings.is12TimeFormat
            binding.swc.setOnCheckedChangeListener { _, isChecked ->
                listener.onChangeTimeFormat(isChecked)
            }
        }
        if (settingsItem == SettingsItem.MONET) {
            binding.swc.isChecked = settings.monet
            binding.swc.setOnCheckedChangeListener { _, isChecked ->
                listener.onChangeMonetTheme(isChecked)
            }
        }
    }
}