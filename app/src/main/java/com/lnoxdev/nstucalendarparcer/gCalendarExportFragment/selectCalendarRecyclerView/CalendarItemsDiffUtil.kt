package com.lnoxdev.nstucalendarparcer.gCalendarExportFragment.selectCalendarRecyclerView

import androidx.recyclerview.widget.DiffUtil
import com.lnoxdev.nstucalendarparcer.models.CalendarItem

class CalendarItemsDiffUtil(
    private val oldList: List<CalendarItem>,
    private val newList: List<CalendarItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }

}