package com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.lnoxdev.nstucalendarparcer.R
import com.lnoxdev.nstucalendarparcer.databinding.ItemSettingsThemeBinding
import com.lnoxdev.nstucalendarparcer.models.SettingsUiState
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.SettingsItem
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.SettingsRecyclerViewAdapter
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders.themeRecyclerView.SettingsThemeRecyclerViewAdapter
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders.themeRecyclerView.ThemeRecyclerViewItemDecorator

class SettingsThemeViewHolder(
    view: View,
    listener: SettingsRecyclerViewAdapter.SettingsRecyclerViewListener
) : SettingsItemViewHolder(view, listener) {
    private val binding = ItemSettingsThemeBinding.bind(view)
    override fun bind(settings: SettingsUiState, settingsItem: SettingsItem) {
        if (!settings.monet) {
            itemView.visibility = View.VISIBLE
        } else {
            itemView.visibility = View.GONE
        }

        binding.tvTitle.text = itemView.context.getString(settingsItem.titleResId)
        binding.tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(
            settingsItem.iconResId,
            0,
            0,
            0
        )
        binding.rvThemes.adapter = SettingsThemeRecyclerViewAdapter(settings, listener)
        binding.rvThemes.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        val margin = itemView.context.resources.getDimension(R.dimen.theme_margin).toInt()
        binding.rvThemes.addItemDecoration(ThemeRecyclerViewItemDecorator(margin))
    }
}


