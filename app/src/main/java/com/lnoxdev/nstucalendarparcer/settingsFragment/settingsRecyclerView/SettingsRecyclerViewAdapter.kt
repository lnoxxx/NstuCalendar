package com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lnoxdev.nstucalendarparcer.R
import com.lnoxdev.nstucalendarparcer.models.SettingsUiState
import com.lnoxdev.nstucalendarparcer.models.UiAppTheme
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders.SettingsItemViewHolder
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders.SettingsSelectedViewHolder
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders.SettingsSwitchViewHolder
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders.SettingsThemeViewHolder

class SettingsRecyclerViewAdapter(
    private val listener: SettingsListener
) : RecyclerView.Adapter<SettingsItemViewHolder>() {

    interface SettingsListener {
        fun onChangeTimeFormat(is12TimeFormat: Boolean)
        fun onChangeGroup()
        fun onChangeMonetTheme(enable: Boolean)
        fun onChangeTheme(theme: UiAppTheme, themeRvState: Parcelable?)
        fun onChangeLanguage()
        fun onChangeDarkMode()
    }

    private var settings: SettingsUiState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsItemViewHolder {
        return when (viewType) {
            SettingsItem.GROUP.ordinal -> createSettingsSelectedViewHolder(parent)
            SettingsItem.TIME_FORMAT.ordinal -> createSettingsSwitchViewHolder(parent)
            SettingsItem.MONET.ordinal -> createSettingsSwitchViewHolder(parent)
            SettingsItem.THEME.ordinal -> createSettingsThemeViewHolder(parent)
            SettingsItem.LANGUAGE.ordinal -> createSettingsSelectedViewHolder(parent)
            SettingsItem.DARK_MODE.ordinal -> createSettingsSelectedViewHolder(parent)
            else -> throw IllegalStateException("SettingsRecyclerViewAdapter unknown viewType!")
        }
    }

    override fun getItemCount(): Int = SettingsItem.entries.size

    override fun onBindViewHolder(holder: SettingsItemViewHolder, position: Int) {
        val settingsItem = SettingsItem.entries[position]
        settings?.let { holder.bind(it, settingsItem) }
    }

    fun updateSettings(newSettings: SettingsUiState) {
        val diffResult = DiffUtil.calculateDiff(SettingsRecyclerViewDiffUtil(settings, newSettings))
        settings = newSettings
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        return SettingsItem.entries[position].ordinal
    }

    private fun createView(layout: Int, parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(layout, parent, false)
    }

    private fun createSettingsSelectedViewHolder(parent: ViewGroup): SettingsSelectedViewHolder {
        val view = createView(R.layout.item_settings_selected, parent)
        return SettingsSelectedViewHolder(view, listener)
    }

    private fun createSettingsSwitchViewHolder(parent: ViewGroup): SettingsSwitchViewHolder {
        val view = createView(R.layout.item_settings_switch, parent)
        return SettingsSwitchViewHolder(view, listener)
    }

    private fun createSettingsThemeViewHolder(parent: ViewGroup): SettingsThemeViewHolder {
        val view = createView(R.layout.item_settings_theme, parent)
        return SettingsThemeViewHolder(view, listener, settings?.themeRvState)
    }

}