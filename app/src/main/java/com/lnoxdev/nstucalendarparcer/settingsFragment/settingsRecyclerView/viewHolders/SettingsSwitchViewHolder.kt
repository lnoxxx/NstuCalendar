package com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders

import android.os.Build
import android.view.View
import com.lnoxdev.nstucalendarparcer.databinding.ItemSettingsSwitchBinding
import com.lnoxdev.nstucalendarparcer.models.SettingsUiState
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.SettingsItem
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.SettingsRecyclerViewAdapter

class SettingsSwitchViewHolder(
    view: View,
    listener: SettingsRecyclerViewAdapter.SettingsListener
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
        binding.swc.isEnabled = true
        binding.tvTitle.alpha = 1f
        if (settingsItem == SettingsItem.TIME_FORMAT) {
            binding.swc.isChecked = settings.is12TimeFormat
            binding.swc.setOnCheckedChangeListener { _, isChecked ->
                listener.onChangeTimeFormat(isChecked)
            }
        }
        if (settingsItem == SettingsItem.MONET) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                binding.swc.isEnabled = false
                binding.tvTitle.alpha = 0.5f
            }
            binding.swc.isChecked = settings.monet
            binding.swc.setOnCheckedChangeListener { _, isChecked ->
                listener.onChangeMonetTheme(isChecked)
            }
        }
    }
}