package com.lnoxdev.nstucalendarparcer.settingsFragment.selectGroup.groupRecyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lnoxdev.nstucalendarparcer.databinding.ItemGroupBinding

class GroupViewHolder(
    view: View,
    private val listener: GroupRecyclerViewListener,
) : RecyclerView.ViewHolder(view) {

    private val binding = ItemGroupBinding.bind(view)

    fun bind(group: String) {
        binding.tvGroup.text = group
        itemView.setOnClickListener {
            listener.onClickGroup(group)
        }
    }
}