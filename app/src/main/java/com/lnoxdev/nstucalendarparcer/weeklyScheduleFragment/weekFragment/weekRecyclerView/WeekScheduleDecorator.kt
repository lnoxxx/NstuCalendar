package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment.weekRecyclerView

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class WeekScheduleDecorator(private val margin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = margin
            }
            left = margin
            right = margin
            bottom = margin
        }
    }

}