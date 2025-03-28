package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment.weekRecyclerView.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lnoxdev.nstucalendarparcer.databinding.ItemLessonBinding
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleLesson

class LessonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemLessonBinding.bind(view)
    fun bind(lesson: WeekScheduleLesson) {
        val lessonType = itemView.context.getString(lesson.type.stringResource)
        val location = lesson.cabinet
        val lessonName = lesson.name
        val lessonTimeStart = lesson.startTime.toString()
        val lessonTimeEnd = lesson.endTime.toString()
        val lessonTime = "$lessonTimeStart:$lessonTimeEnd"
        val teacher = lesson.teacher?.name

        with(binding) {
            tvLessonType.text = lessonType
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
