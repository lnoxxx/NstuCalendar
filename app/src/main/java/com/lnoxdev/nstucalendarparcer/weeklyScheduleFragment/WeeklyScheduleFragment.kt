package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.lnoxdev.nstucalendarparcer.databinding.FragmentWeeklyScheduleBinding
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                state?.let {
                    it.weeksCount?.let { it1 -> initViewPager(it1) }
                }
            }
        }
    }

    private fun initViewPager(weeksCount: Int) {
        val oldWeeksCount = binding.vpWeeks.adapter?.itemCount
        if (oldWeeksCount != weeksCount)
            binding.vpWeeks.adapter = WeeksPagerAdapter(this, weeksCount)
    }
}