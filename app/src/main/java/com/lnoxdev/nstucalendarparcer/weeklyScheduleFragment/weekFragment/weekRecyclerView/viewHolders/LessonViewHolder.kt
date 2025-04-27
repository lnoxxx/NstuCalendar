package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment.weekRecyclerView.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lnoxdev.nstucalendarparcer.databinding.ItemScheduleLessonBinding
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleLesson
import com.lnoxdev.nstucalendarparcer.utils.getThemeColor
import com.lnoxdev.nstucalendarparcer.utils.getTimeFormat

class LessonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemScheduleLessonBinding.bind(view)
    fun bind(lesson: WeekScheduleLesson) {
        val lessonType = itemView.context.getString(lesson.type.stringResource)
        val colorType = lesson.type.colorResId?.let { itemView.context.getColor(it) }
        val location = lesson.cabinet
        val lessonName = lesson.name
        val lessonTimeStart = lesson.startTime.format(getTimeFormat(lesson.is12HourTimeFormat))
        val lessonTimeEnd = lesson.endTime.format(getTimeFormat(lesson.is12HourTimeFormat))
        val lessonTime = "$lessonTimeStart - $lessonTimeEnd"
        val teacher = lesson.teacher?.name
        with(binding) {
            tvLessonType.text = lessonType
            if (colorType == null) {
                val defaultTextColor =
                    itemView.context.getThemeColor(com.google.android.material.R.attr.colorOnSurface)
                tvLessonType.setTextColor(defaultTextColor)
            } else {
                tvLessonType.setTextColor(colorType)
            }
            if (location != null) {
                tvCabinet.visibility = View.VISIBLE
                tvCabinet.text = location
            } else {
                tvCabinet.visibility = View.GONE
            }
            tvLessonName.text = lessonName
            tvLessonTime.text = lessonTime
            if (teacher != null) {
                tvTeacherName.visibility = View.VISIBLE
                tvTeacherName.text = teacher
            } else {
                tvTeacherName.visibility = View.GONE
            }
        }
    }
}
