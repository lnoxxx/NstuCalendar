package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import com.lnoxdev.nstucalendarparcer.NavigationActivity
import com.lnoxdev.nstucalendarparcer.R
import com.lnoxdev.nstucalendarparcer.TransitionAnimations
import com.lnoxdev.nstucalendarparcer.databinding.FragmentWeeklyScheduleBinding
import com.lnoxdev.nstucalendarparcer.exceptions.showErrorSnackBar
import com.lnoxdev.nstucalendarparcer.models.UiScheduleExceptions
import com.lnoxdev.nstucalendarparcer.models.WeeklyScheduleState
import com.lnoxdev.nstucalendarparcer.utils.getDateTimeFormatter
import com.lnoxdev.nstucalendarparcer.utils.getThemeColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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
        TransitionAnimations.initFadeAnimation(this)
        binding.ablWeeklySchedule.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(context)
        binding.ablWeeklySchedule
            .setStatusBarForegroundColor(
                requireContext()
                    .getThemeColor(com.google.android.material.R.attr.colorSurfaceContainer)
            )
        hideHello()
        navigationInit()
        binding.cpiScheduleLoading.hide()
        binding.imvError.visibility = View.GONE
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
        binding.imvError.setOnClickListener {
            viewModel.exception.value?.let { binding.root.showErrorSnackBar(it) }
        }
        lifecycleScope.launch {
            viewModel.isUpdate.collectLatest {
                if (it) binding.cpiScheduleLoading.show() else binding.cpiScheduleLoading.hide()
            }
        }
        lifecycleScope.launch {
            viewModel.exception.collect {
                if (it == UiScheduleExceptions.SETTING_GROUP) {
                    return@collect
                }
                if (it != null) {
                    binding.imvError.visibility = View.VISIBLE
                } else {
                    binding.imvError.visibility = View.GONE
                }
            }
        }
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                state?.let { bindUiState(it) }
            }
        }
    }

    private fun navigationInit() {
        binding.tbWeeklySchedule.setNavigationOnClickListener {
            (activity as NavigationActivity).openDrawer()
        }
    }

    private fun bindUiState(state: WeeklyScheduleState) {
        binding.tvGroup.text = state.group ?: getString(R.string.select_group)
        val updateTime = state.lastUpdateTime
        binding.tvLastUpdateTime.text =
            updateTime?.format(getDateTimeFormatter(state.is12HourTimeFormat))
                ?: getString(R.string.not_update)
        state.weeksCount?.let { initViewPager(it, state.nowWeekIndex) }
        if (state.group == null) {
            showHello()
        } else {
            hideHello()
        }
    }

    private fun showHello() {
        binding.tlWeeks.visibility = View.GONE
        val clPadding = resources.getDimension(R.dimen.app_bar_margin).toInt()
        binding.clAppBarContentContainer.updatePadding(bottom = clPadding)
        binding.btnSelectGroup.visibility = View.VISIBLE
        binding.btnSelectGroup.setOnClickListener {
            findNavController().navigate(R.id.selectGroupBottomSheet)
        }
    }

    private fun hideHello() {
        binding.tlWeeks.visibility = View.VISIBLE
        binding.clAppBarContentContainer.updatePadding(bottom = 0)
        binding.btnSelectGroup.visibility = View.GONE
    }

    private fun initViewPager(weeksCount: Int, nowWeek: Int?) {
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
        val nowWeek = viewModel.uiState.value?.nowWeekIndex ?: 0
        TabLayoutMediator(binding.tlWeeks, binding.vpWeeks) { tab, position ->
            if (position < nowWeek)
                tab.view.alpha = 0.60f
            tab.text = "${position + 1} $weekString"
        }.attach()
    }
}