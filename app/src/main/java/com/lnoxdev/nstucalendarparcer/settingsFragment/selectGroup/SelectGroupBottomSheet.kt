package com.lnoxdev.nstucalendarparcer.settingsFragment.selectGroup

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lnoxdev.nstucalendarparcer.R
import com.lnoxdev.nstucalendarparcer.databinding.BottomSheetSelectGroupBinding
import com.lnoxdev.nstucalendarparcer.models.UiExceptions
import com.lnoxdev.nstucalendarparcer.settingsFragment.selectGroup.groupRecyclerView.GroupDecorator
import com.lnoxdev.nstucalendarparcer.settingsFragment.selectGroup.groupRecyclerView.GroupRecyclerViewAdapter
import com.lnoxdev.nstucalendarparcer.settingsFragment.selectGroup.groupRecyclerView.GroupRecyclerViewListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectGroupBottomSheet : BottomSheetDialogFragment(), GroupRecyclerViewListener {

    private var _binding: BottomSheetSelectGroupBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("SelectGroupBottomSheet binding null!")

    private val viewModel: SelectGroupViewModel by viewModels()

    private val adapter = GroupRecyclerViewAdapter(this)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = BottomSheetSelectGroupBinding.inflate(LayoutInflater.from(context))
        val dialog =
            BottomSheetDialog(requireContext(), R.style.ThemeOverlay_NstuCalendar_BottomSheet)
        dialog.setContentView(binding.root)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        setOnTextChangeListener()
        initRecyclerView()
        binding.tvException.visibility = View.GONE
        lifecycleScope.launch {
            viewModel.groups.collect {
                it?.let { bindGroupList(it) }
            }
        }
        lifecycleScope.launch {
            viewModel.exception.collect {
                it?.let { bindException(it) }
            }
        }
        return dialog
    }

    private fun setOnTextChangeListener() {
        binding.etSearch.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.search(text.toString())
        }
    }

    private fun initRecyclerView() {
        binding.rvGroups.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvGroups.adapter = adapter
        val margin = requireContext().resources.getDimension(R.dimen.group_margin).toInt()
        binding.rvGroups.addItemDecoration(GroupDecorator(margin))
    }

    private fun bindGroupList(groups: List<String>) {
        binding.tvException.visibility = View.GONE
        adapter.updateGroupList(groups)
    }

    private fun bindException(exception: UiExceptions) {
        binding.tvException.visibility = View.VISIBLE
        adapter.clear()
        binding.tvException.text = getString(exception.textResId)
    }

    override fun onClickGroup(group: String) {
        viewModel.selectGroup(group)
        findNavController().navigateUp()
    }
}