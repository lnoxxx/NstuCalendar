package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment.weekRecyclerView.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lnoxdev.nstucalendarparcer.databinding.ItemNowLessonTimeBinding
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleNowLessonTime

class NowLessonTimeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemNowLessonTimeBinding.bind(view)
    fun bind(time: WeekScheduleNowLessonTime) {
        val timeStart = time.timeStart.toString()
        val timeEnd = time.timeEnd.toString()
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