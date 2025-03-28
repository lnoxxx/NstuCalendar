package com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lnoxdev.nstucalendarparcer.databinding.FragmentWeekBinding
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleItem
import com.lnoxdev.nstucalendarparcer.models.WeekScheduleUiState
import com.lnoxdev.nstucalendarparcer.weeklyScheduleFragment.weekFragment.weekRecyclerView.WeekRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeekFragment : Fragment() {

    private var _binding: FragmentWeekBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Week fragment binding null!")

    private var _adapter: WeekRecyclerViewAdapter? = null
    private val adapter
        get() = _adapter ?: throw IllegalStateException("Week fragment rvAdapter null!")

    private val viewModel: WeekViewModel by lazy {
        val viewModel: WeekViewModel by viewModels()
        val weekIndex = arguments?.getInt(WEEK_INDEX_KEY)
            ?: throw IllegalStateException("Week fragment not find argument WEEK_INDEX_KEY")
        viewModel.setWeekIndex(weekIndex)
        viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeekBinding.inflate(inflater, container, false)
        initRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                state?.let { bindNewUiState(it) }
            }
        }
    }

    private fun initRecyclerView() {
        _adapter = WeekRecyclerViewAdapter()
        binding.rvWeekSchedule.adapter = adapter
        binding.rvWeekSchedule.layoutManager = LinearLayoutManager(context)
    }

    private fun bindNewUiState(state: WeekScheduleUiState) {
        state.schedule?.let { updateSchedule(it) }
    }

    private fun updateSchedule(schedule: List<WeekScheduleItem>) {
        adapter.updateSchedule(schedule)
    }

    companion object {
        private const val WEEK_INDEX_KEY = "WeekFragment week index"
        fun newInstance(weekIndex: Int): WeekFragment {
            val fragment = WeekFragment()
            val bundle = Bundle()
            bundle.putInt(WEEK_INDEX_KEY, weekIndex)
            fragment.arguments = bundle
            return fragment
        }
    }
}