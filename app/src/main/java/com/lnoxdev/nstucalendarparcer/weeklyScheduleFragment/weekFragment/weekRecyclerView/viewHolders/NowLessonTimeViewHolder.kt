package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment.weekRecyclerView.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lnoxdev.nstucalendarparcer.databinding.ItemScheduleNowLessonTimeBinding
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleNowLessonTime
import com.lnoxdev.nstucalendarparcer.utils.getTimeFormat

class NowLessonTimeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemScheduleNowLessonTimeBinding.bind(view)
    fun bind(time: WeekScheduleNowLessonTime) {
        val formatter = getTimeFormat(time.is12HourTimeFormat)
        val timeStart = time.timeStart.format(formatter)
        val timeEnd = time.timeEnd.format(formatter)
        val progress = time.progress
        with(binding) {
            tvStartTime.text = timeStart
            tvEndTime.text = timeEnd
            piLessonProgress.min = 0
            piLessonProgress.max = 100
            piLessonProgress.progress = progress
        }
    }
}