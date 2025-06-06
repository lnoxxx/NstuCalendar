package com.lnoxdev.nstucalendarparcer.gCalendarExportFragment.selectCalendarRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lnoxdev.nstucalendarparcer.R
import com.lnoxdev.nstucalendarparcer.models.CalendarItem

class CalendarItemsAdapter(
    private val listener: SelectCalendarListener
) : RecyclerView.Adapter<CalendarItemViewHolder>() {
    private val items = mutableListOf<CalendarItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return CalendarItemViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CalendarItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateItems(newList: List<CalendarItem>) {
        val diffCallback = DiffUtil.calculateDiff(CalendarItemsDiffUtil(items, newList))
        items.clear()
        items.addAll(newList)
        diffCallback.dispatchUpdatesTo(this)
    }
}