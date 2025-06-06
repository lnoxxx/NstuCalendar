package com.lnoxdev.nstucalendarparcer.gCalendarExportFragment.selectCalendarRecyclerView

import com.lnoxdev.nstucalendarparcer.models.CalendarItem

interface SelectCalendarListener {
    fun selectCalendar(calendar: CalendarItem)
}