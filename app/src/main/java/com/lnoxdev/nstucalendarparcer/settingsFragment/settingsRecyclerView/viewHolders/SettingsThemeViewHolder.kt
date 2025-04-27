package com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders

import android.os.Parcelable
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
    listener: SettingsRecyclerViewAdapter.SettingsListener,
    themeRvState: Parcelable?,
) : SettingsItemViewHolder(view, listener) {
    private val binding = ItemSettingsThemeBinding.bind(view)
    private val layoutManager =
        LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
    private val adapter = SettingsThemeRecyclerViewAdapter(listener, layoutManager)
    private val margin = itemView.context.resources.getDimension(R.dimen.theme_margin).toInt()

    init {
        binding.rvThemes.adapter = adapter
        binding.rvThemes.layoutManager = layoutManager
        layoutManager.onRestoreInstanceState(themeRvState)
        binding.rvThemes.addItemDecoration(ThemeRecyclerViewItemDecorator(margin))
    }

    override fun bind(settings: SettingsUiState, settingsItem: SettingsItem) {
        binding.tvTitle.text = itemView.context.getString(settingsItem.titleResId)
        binding.tvTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(
            settingsItem.iconResId,
            0,
            0,
            0
        )
        if (settings.monet){
            binding.rvThemes.alpha = 0.5f
        }else{
            binding.rvThemes.alpha = 1f
        }
        adapter.changeSelectedTheme(settings)
    }
}


