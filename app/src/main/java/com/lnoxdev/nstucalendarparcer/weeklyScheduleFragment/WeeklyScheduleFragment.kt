package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import com.lnoxdev.nstucalendarparcer.R
import com.lnoxdev.nstucalendarparcer.databinding.FragmentWeeklyScheduleBinding
import com.lnoxdev.nstucalendarparcer.exceptions.showErrorSnackBar
import com.lnoxdev.nstucalendarparcer.models.UiExceptions
import com.lnoxdev.nstucalendarparcer.models.WeeklyScheduleState
import com.lnoxdev.nstucalendarparcer.utils.getDateTimeFormatter
import com.lnoxdev.nstucalendarparcer.utils.getThemeColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeeklyScheduleFragment : Fragment() {

    private var _binding: FragmentWeeklyScheduleBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Weekly schedule binding null!")

    private val viewModel: WeeklyScheduleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeeklyScheduleBinding.inflate(inflater, container, false)
        // fix edgeToEdge
        binding.ablWeeklySchedule.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(context)
        binding.ablWeeklySchedule
            .setStatusBarForegroundColor(
                requireContext()
                    .getThemeColor(com.google.android.material.R.attr.colorSurfaceContainer)
            )
        // default visibility
        binding.tvGroup.visibility = View.GONE
        binding.tvLastUpdateTime.visibility = View.GONE
        binding.tlWeeks.visibility = View.GONE
        binding.btnSelectGroup.visibility = View.GONE
        binding.cpiScheduleLoading.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tbWeeklySchedule.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuItemToday -> {
                    val index = viewModel.uiState.value?.nowWeekIndex
                        ?: return@setOnMenuItemClickListener true
                    scrollToWeek(index)
                    true
                }

                else -> false
            }
        }
        binding.tvGroup.setOnClickListener {
            findNavController().navigate(R.id.selectGroupBottomSheet)
        }
        binding.tvLastUpdateTime.setOnClickListener {
            viewModel.startUpdateSchedule()
        }
        binding.btnSelectGroup.setOnClickListener {
            findNavController().navigate(R.id.selectGroupBottomSheet)
        }
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                state?.let { bindUiState(it) }
            }
        }
        lifecycleScope.launch {
            viewModel.isUpdate.collect {
                if (it) binding.cpiScheduleLoading.show() else binding.cpiScheduleLoading.hide()
            }
        }
        lifecycleScope.launch {
            viewModel.exception.collect {
                if (it == UiExceptions.SETTING_GROUP) {
                    binding.btnSelectGroup.visibility = View.VISIBLE
                    return@collect
                }
                it?.let { binding.root.showErrorSnackBar(it) }
            }
        }
    }

    private fun bindUiState(state: WeeklyScheduleState) {
        val group = state.group
        if (group != null) {
            binding.btnSelectGroup.visibility = View.GONE
            binding.tvGroup.visibility = View.VISIBLE
            binding.tvGroup.text = group
        } else {
            binding.btnSelectGroup.visibility = View.VISIBLE
            binding.tvGroup.visibility = View.GONE
        }
        val updateTime = state.lastUpdateTime
        if (updateTime != null) {
            binding.tvLastUpdateTime.visibility = View.VISIBLE
            binding.tvLastUpdateTime.text =
                updateTime.format(getDateTimeFormatter(state.is12HourTimeFormat))
                    ?: getString(R.string.not_update)
        } else {
            binding.tvLastUpdateTime.visibility = View.GONE
        }
        state.weeksCount?.let { initViewPager(it, state.nowWeekIndex) }
    }

    private fun initViewPager(weeksCount: Int, nowWeek: Int?) {
        binding.tlWeeks.visibility = View.VISIBLE
        val oldWeeksCount = binding.vpWeeks.adapter?.itemCount
        if (oldWeeksCount != weeksCount) {
            binding.vpWeeks.adapter = WeeksPagerAdapter(this, weeksCount)
            initTabLayout()
            nowWeek?.let { scrollToWeek(it) }
        }
    }

    private fun scrollToWeek(index: Int) {
        binding.vpWeeks.setCurrentItem(index, true)
    }

    private fun initTabLayout() {
        val weekString = getString(R.string.tab_week)
        TabLayoutMediator(binding.tlWeeks, binding.vpWeeks) { tab, position ->
            tab.text = "${position + 1} $weekString"
        }.attach()
    }
}