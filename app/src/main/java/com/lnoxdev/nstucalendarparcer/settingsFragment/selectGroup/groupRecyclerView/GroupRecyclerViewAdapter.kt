package com.lnoxdev.nstucalendarparcer.settingsFragment.selectGroup.groupRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lnoxdev.nstucalendarparcer.R

class GroupRecyclerViewAdapter(
    private val listener: GroupRecyclerViewListener
) : RecyclerView.Adapter<GroupViewHolder>() {

    private val groupList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return GroupViewHolder(view, listener)
    }

    override fun getItemCount(): Int = groupList.count()

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(groupList[position])
    }

    fun updateGroupList(newGroupList: List<String>) {
        val diff = DiffUtil.calculateDiff(GroupDiffUtil(groupList, newGroupList))
        groupList.clear()
        groupList.addAll(newGroupList)
        diff.dispatchUpdatesTo(this)
    }

    fun clear() {
        val diff = DiffUtil.calculateDiff(GroupDiffUtil(groupList, listOf()))
        groupList.clear()
        diff.dispatchUpdatesTo(this)
    }
}