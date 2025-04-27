package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment.weekRecyclerView.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lnoxdev.nstucalendarparcer.databinding.ItemScheduleLessonPastBinding
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleLesson
import com.lnoxdev.nstucalendarparcer.utils.getTimeFormat

class LessonPastViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemScheduleLessonPastBinding.bind(view)
    fun bind(lesson: WeekScheduleLesson) {
        val lessonName = lesson.name
        val lessonTimeStart = lesson.startTime.format(getTimeFormat(lesson.is12HourTimeFormat))
        val lessonTimeEnd = lesson.endTime.format(getTimeFormat(lesson.is12HourTimeFormat))
        val lessonTime = "$lessonTimeStart - $lessonTimeEnd"
        with(binding) {
            tvLessonName.text = lessonName
            tvLessonTime.text = lessonTime
        }
    }
}