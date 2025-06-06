package com.lnoxdev.nstucalendarparcer.gCalendarExportFragment.selectCalendarRecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.lnoxdev.nstucalendarparcer.databinding.ItemCalendarBinding
import com.lnoxdev.nstucalendarparcer.models.CalendarItem
import com.lnoxdev.nstucalendarparcer.utils.getThemeColor

class CalendarItemViewHolder(
    view: View,
    private val listener: SelectCalendarListener
) : ViewHolder(view) {
    private val binding = ItemCalendarBinding.bind(view)
    private val selectedBackgroundColor =
        itemView.context.getThemeColor(com.google.android.material.R.attr.colorPrimaryContainer)
    private val unselectedBackgroundColor =
        itemView.context.getThemeColor(com.google.android.material.R.attr.colorSurfaceContainerHighest)

    fun bind(item: CalendarItem) {
        binding.tvCalendarName.text = item.name
        val backgroundColor =
            if (item.isSelected) selectedBackgroundColor else unselectedBackgroundColor
        binding.cvCalendar.setCardBackgroundColor(backgroundColor)
        binding.cvCalendar.setOnClickListener {
            listener.selectCalendar(item)
        }
    }
}