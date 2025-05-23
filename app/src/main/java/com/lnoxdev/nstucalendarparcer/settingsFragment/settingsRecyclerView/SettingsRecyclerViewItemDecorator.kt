package com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SettingsRecyclerViewItemDecorator(private val margin: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            bottom = margin
            left = margin
            right = margin
        }
    }

}