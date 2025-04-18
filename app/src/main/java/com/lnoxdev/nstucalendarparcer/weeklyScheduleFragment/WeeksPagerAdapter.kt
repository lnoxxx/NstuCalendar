package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment.WeekFragment

class WeeksPagerAdapter(
    fragment: Fragment,
    private val weeksCount: Int,
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = weeksCount

    override fun createFragment(position: Int): Fragment {
        val newWeek = WeekFragment.newInstance(position)
        return newWeek
    }
}