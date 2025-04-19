package com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.viewHolders.themeRecyclerView

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ThemeRecyclerViewItemDecorator(private val margin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            top = margin
            right = margin
            bottom = margin
        }
    }
}