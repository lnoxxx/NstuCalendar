package com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.lnoxdev.nstucalendarparcer.R
import com.lnoxdev.nstucalendarparcer.databinding.ItemSettingsSelectedBinding
import com.lnoxdev.nstucalendarparcer.models.SettingsUiState
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.SettingsItem
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.SettingsRecyclerViewAdapter

class SettingsSelectedViewHolder(
    view: View,
    listener: SettingsRecyclerViewAdapter.SettingsRecyclerViewListener
) : SettingsItemViewHolder(view, listener) {
    private val binding = ItemSettingsSelectedBinding.bind(view)
    override fun bind(settings: SettingsUiState, settingsItem: SettingsItem) {
        binding.tvTitle.text = itemView.context.getString(settingsItem.titleResId)
        binding.tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(
            settingsItem.iconResId,
            0,
            0,
            0
        )
        if (settingsItem == SettingsItem.GROUP) {
            binding.tvSelectedItem.text =
                settings.group ?: itemView.context.getString(R.string.settings_not_select)
            itemView.setOnClickListener {
                listener.onChangeGroup()
            }
        }
    }
}