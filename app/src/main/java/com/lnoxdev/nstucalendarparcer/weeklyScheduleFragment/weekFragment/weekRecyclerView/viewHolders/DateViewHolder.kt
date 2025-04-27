package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment.weekRecyclerView.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lnoxdev.nstucalendarparcer.databinding.ItemScheduleDateBinding
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleDate
import com.lnoxdev.nstucalendarparcer.utils.getDayOfWeekResource
import com.lnoxdev.nstucalendarparcer.utils.getMonthResources
import com.lnoxdev.nstucalendarparcer.utils.getThemeColor

class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemScheduleDateBinding.bind(view)
    fun bind(date: WeekScheduleDate) {
        val dayOfWeek = itemView.context.getString(getDayOfWeekResource(date.dayOfWeek))
        val month = itemView.context.getString(getMonthResources(date.date.monthValue))
        val dateString = "$month ${date.date.dayOfMonth}"
        val defaultColor =
            itemView.context.getThemeColor(com.google.android.material.R.attr.colorOnSurface)
        val pastColor =
            itemView.context.getThemeColor(com.google.android.material.R.attr.colorOutline)
        val todayColor =
            itemView.context.getThemeColor(com.google.android.material.R.attr.colorPrimary)
        with(binding) {
            tvDayOfWeek.text = dayOfWeek
            tvDate.text = dateString
            tvDate.setTextColor(defaultColor)
            tvDayOfWeek.setTextColor(defaultColor)
            if (date.isFinished) {
                tvDate.setTextColor(pastColor)
                tvDayOfWeek.setTextColor(pastColor)
            }
            if (date.isToday) {
                tvDate.setTextColor(todayColor)
                tvDayOfWeek.setTextColor(todayColor)
            }
        }
    }
}