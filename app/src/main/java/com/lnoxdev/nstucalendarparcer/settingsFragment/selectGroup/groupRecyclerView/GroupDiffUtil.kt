package com.lnoxdev.nstucalendarparcer.settingsFragment.selectGroup.groupRecyclerView

import androidx.recyclerview.widget.DiffUtil

class GroupDiffUtil(
    private val oldGroupList: List<String>,
    private val newGroupList: List<String>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldGroupList.size

    override fun getNewListSize(): Int = newGroupList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldGroupList[oldItemPosition]
        val newItem = newGroupList[newItemPosition]
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldGroupList[oldItemPosition]
        val newItem = newGroupList[newItemPosition]
        return oldItem == newItem
    }

}