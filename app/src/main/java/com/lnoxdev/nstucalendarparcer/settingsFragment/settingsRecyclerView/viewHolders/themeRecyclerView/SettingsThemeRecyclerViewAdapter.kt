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
            val primary = itemView.context.getColor(theme.colorResPrimary)
            val secondary = itemView.context.getColor(theme.colorResSecondary)
            val tertiary = itemView.context.getColor(theme.colorResTertiary)
            val isApplyTheme = settings.appTheme == theme
            val strokeWidth = itemView.context.resources.getDimension(R.dimen.stroke_width_theme).toInt()

            binding.tvThemeName.text = itemView.context.getString(theme.themeNameResId)
            binding.topLeftCorner.backgroundTintList = ColorStateList.valueOf(secondary)
            binding.topRightCorner.backgroundTintList = ColorStateList.valueOf(tertiary)
            binding.bottomCorner.backgroundTintList = ColorStateList.valueOf(primary)
            if (isApplyTheme){
                binding.cvStroke.strokeWidth = strokeWidth
            }else{
                binding.cvStroke.strokeWidth = 0
            }
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