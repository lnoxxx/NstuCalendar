package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment.weekRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.lnoxdev.nstucalendarparcer.R
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleDate
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleItem
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleLesson
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleNowLessonTime
import com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment.weekRecyclerView.viewHolders.DateViewHolder
import com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment.weekRecyclerView.viewHolders.LessonPastViewHolder
import com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment.weekRecyclerView.viewHolders.LessonViewHolder
import com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment.weekRecyclerView.viewHolders.NowLessonTimeViewHolder

class WeekRecyclerViewAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val schedule = mutableListOf<WeekScheduleItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DATE -> {
                val view = inflateView(R.layout.item_schedule_date, parent)
                DateViewHolder(view)
            }

            VIEW_TYPE_LESSON_PAST -> {
                val view = inflateView(R.layout.item_schedule_lesson_past, parent)
                LessonPastViewHolder(view)
            }

            VIEW_TYPE_LESSON -> {
                val view = inflateView(R.layout.item_schedule_lesson, parent)
                LessonViewHolder(view)
            }

            VIEW_TYPE_NOW_TIME -> {
                val view = inflateView(R.layout.item_schedule_now_lesson_time, parent)
                NowLessonTimeViewHolder(view)
            }

            else -> throw IllegalStateException("WeekRecyclerView unknown viewType!")
        }
    }

    private fun inflateView(layoutId: Int, parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
    }

    override fun getItemCount(): Int = schedule.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is DateViewHolder -> {
                val item = schedule[position] as WeekScheduleDate
                holder.bind(item)
            }

            is LessonPastViewHolder -> {
                val item = schedule[position] as WeekScheduleLesson
                holder.bind(item)
            }

            is LessonViewHolder -> {
                val item = schedule[position] as WeekScheduleLesson
                holder.bind(item)
            }

            is NowLessonTimeViewHolder -> {
                val item = schedule[position] as WeekScheduleNowLessonTime
                holder.bind(item)
            }

            else -> throw IllegalStateException("WeekRecyclerView unknown viewType!")
        }
    }

    fun updateSchedule(newSchedule: List<WeekScheduleItem>) {
        val diffResult = DiffUtil.calculateDiff(WeekScheduleDiffUtil(schedule, newSchedule))
        schedule.clear()
        schedule.addAll(newSchedule)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = schedule[position]) {
            is WeekScheduleDate -> VIEW_TYPE_DATE
            is WeekScheduleLesson -> if (item.isFinished) VIEW_TYPE_LESSON_PAST else VIEW_TYPE_LESSON
            is WeekScheduleNowLessonTime -> VIEW_TYPE_NOW_TIME
        }
    }

    companion object {
        const val VIEW_TYPE_LESSON = 1
        const val VIEW_TYPE_LESSON_PAST = 2
        const val VIEW_TYPE_DATE = 3
        const val VIEW_TYPE_NOW_TIME = 4
    }

}