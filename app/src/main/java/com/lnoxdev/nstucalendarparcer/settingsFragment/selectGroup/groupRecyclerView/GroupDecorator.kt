package com.lnoxdev.nstucalendarparcer.settingsFragment.selectGroup.groupRecyclerView

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GroupDecorator(private val margin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            top = margin
            left = margin / 2
            right = margin / 2
        }
    }
}