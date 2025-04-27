package com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders.themeRecyclerView

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lnoxdev.nstucalendarparcer.R
import com.lnoxdev.nstucalendarparcer.databinding.ItemThemeBinding
import com.lnoxdev.nstucalendarparcer.models.SettingsUiState
import com.lnoxdev.nstucalendarparcer.models.UiAppTheme
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.SettingsRecyclerViewAdapter

class SettingsThemeRecyclerViewAdapter(
    private val listener: SettingsRecyclerViewAdapter.SettingsListener,
    private val layoutManager: LinearLayoutManager,
) : RecyclerView.Adapter<SettingsThemeRecyclerViewAdapter.ThemeItemViewHolder>() {
    private var selectedTheme: UiAppTheme? = null
    private var enable = true

    inner class ThemeItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemThemeBinding.bind(view)
        private val strokeWidth =
            itemView.context.resources.getDimension(R.dimen.stroke_width_theme)

        fun bind(theme: UiAppTheme) {
            val primary = itemView.context.getColor(theme.colorResPrimary)
            val secondary = itemView.context.getColor(theme.colorResSecondary)
            val tertiary = itemView.context.getColor(theme.colorResTertiary)
            val isApplyTheme = selectedTheme == theme
            with(binding) {
                tvThemeName.text = itemView.context.getString(theme.themeNameResId)
                topLeftCorner.backgroundTintList = ColorStateList.valueOf(secondary)
                topRightCorner.backgroundTintList = ColorStateList.valueOf(tertiary)
                bottomCorner.backgroundTintList = ColorStateList.valueOf(primary)
                if (isApplyTheme) {
                    cvStroke.strokeWidth = strokeWidth.toInt()
                } else {
                    cvStroke.strokeWidth = 0
                }
            }
            itemView.setOnClickListener {
                if (!enable) return@setOnClickListener
                val state = layoutManager.onSaveInstanceState()
                listener.onChangeTheme(theme, state)
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

    fun changeSelectedTheme(newSettings: SettingsUiState) {
        val callback = SettingsThemeDiffUtil(newSettings.appTheme, selectedTheme)
        val diffResult = DiffUtil.calculateDiff(callback)
        selectedTheme = newSettings.appTheme
        enable = !newSettings.monet
        diffResult.dispatchUpdatesTo(this)
    }

}