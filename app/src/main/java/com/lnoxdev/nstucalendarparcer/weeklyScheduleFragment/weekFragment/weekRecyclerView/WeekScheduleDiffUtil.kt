package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment.weekRecyclerView

import androidx.recyclerview.widget.DiffUtil
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleDate
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleItem
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleLesson
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleNowLessonTime

class WeekScheduleDiffUtil(
    private val oldSchedule: List<WeekScheduleItem>,
    private val newSchedule: List<WeekScheduleItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldSchedule.size

    override fun getNewListSize(): Int = newSchedule.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldSchedule[oldItemPosition]
        val newItem = newSchedule[newItemPosition]
        return when (oldItem) {
            is WeekScheduleDate -> {
                if (newItem !is WeekScheduleDate)
                    false
                else
                    oldItem.date == newItem.date
            }

            is WeekScheduleLesson -> {
                if (newItem !is WeekScheduleLesson)
                    false
                else
                    (oldItem.startTime == newItem.startTime)
                            && (oldItem.name == newItem.name) && (oldItem.index == newItem.index)
            }

            is WeekScheduleNowLessonTime -> {
                if (newItem !is WeekScheduleNowLessonTime)
                    false
                else
                    (oldItem.timeEnd == newItem.timeEnd) && (oldItem.timeStart == newItem.timeStart)
            }
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldSchedule[oldItemPosition]
        val newItem = newSchedule[newItemPosition]
        return when (oldItem) {
            is WeekScheduleDate -> {
                if (newItem !is WeekScheduleDate)
                    false
                else
                    oldItem == newItem
            }

            is WeekScheduleLesson -> {
                if (newItem !is WeekScheduleLesson)
                    false
                else
                    oldItem == newItem
            }

            is WeekScheduleNowLessonTime -> {
                if (newItem !is WeekScheduleNowLessonTime)
                    false
                else
                    oldItem == newItem
            }
        }
    }

}