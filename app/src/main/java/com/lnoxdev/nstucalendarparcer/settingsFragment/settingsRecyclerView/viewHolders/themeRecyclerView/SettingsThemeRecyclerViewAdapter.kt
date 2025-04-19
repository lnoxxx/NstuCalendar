package com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders.themeRecyclerView

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lnoxdev.nstucalendarparcer.R
import com.lnoxdev.nstucalendarparcer.databinding.ItemThemeBinding
import com.lnoxdev.nstucalendarparcer.models.SettingsUiState
import com.lnoxdev.nstucalendarparcer.models.UiAppTheme
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.SettingsRecyclerViewAdapter

class SettingsThemeRecyclerViewAdapter(
    private val settings: SettingsUiState,
    private val listener: SettingsRecyclerViewAdapter.SettingsRecyclerViewListener
) : RecyclerView.Adapter<SettingsThemeRecyclerViewAdapter.ThemeItemViewHolder>() {

    inner class ThemeItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemThemeBinding.bind(view)
        fun bind(theme: UiAppTheme) {
            val backgroundColor = itemView.context.getColor(theme.colorResPrimary)
            val textColor = itemView.context.getColor(theme.colorResOnPrimary)
            binding.tvThemeName.text = itemView.context.getString(theme.themeNameResId)
            binding.cvBackground.setCardBackgroundColor(backgroundColor)
            binding.tvThemeName.setTextColor(textColor)
            binding.cvBackground.checkedIconTint = ColorStateList.valueOf(textColor)
            binding.cvBackground.isChecked = settings.appTheme == theme
            itemView.setOnClickListener {
                listener.onChangeTheme(theme)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_theme, parent, false)
        return ThemeItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return UiAppTheme.entries.size
    }

    override fun onBindViewHolder(holder: ThemeItemViewHolder, position: Int) {
        holder.bind(UiAppTheme.entries[position])
    }

}