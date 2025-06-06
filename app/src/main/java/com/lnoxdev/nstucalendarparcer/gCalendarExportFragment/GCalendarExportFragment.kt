package com.lnoxdev.nstucalendarparcer.gCalendarExportFragment

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lnoxdev.nstucalendarparcer.NavigationActivity
import com.lnoxdev.nstucalendarparcer.TransitionAnimations
import com.lnoxdev.nstucalendarparcer.databinding.FragmentGCalendarExportBinding
import com.lnoxdev.nstucalendarparcer.gCalendarExportFragment.selectCalendarRecyclerView.CalendarItemsAdapter
import com.lnoxdev.nstucalendarparcer.gCalendarExportFragment.selectCalendarRecyclerView.SelectCalendarListener
import com.lnoxdev.nstucalendarparcer.models.CalendarItem
import com.lnoxdev.nstucalendarparcer.models.GCalendarUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// in development!

@AndroidEntryPoint
class GCalendarExportFragment : Fragment(), SelectCalendarListener {

    private var _binding: FragmentGCalendarExportBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("GCalendarExportFragment binding null!")

    private val viewModel: GCalendarExportViewModel by viewModels()

    private val calendarAdapter = CalendarItemsAdapter(this)

    private val requestCalendarPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                onCalendarPermissionsGranted()
            } else {
                onCalendarPermissionsDenied()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGCalendarExportBinding.inflate(inflater)
        TransitionAnimations.initFadeAnimation(this)
        initNavigation()
        initCalendarRecyclerView()
        binding.clMain.setOnApplyWindowInsetsListener { v, insets ->
            val windowInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(bottom = windowInsets.bottom)
            return@setOnApplyWindowInsetsListener insets
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arePermissionsGranted()) {
            onCalendarPermissionsGranted()
        } else {
            onCalendarPermissionsDenied()
        }
        binding.btnExport.setOnClickListener {
            val time = binding.etReminderTime.editText?.text.toString()
            viewModel.export(time)
        }
        binding.btnGrandPermission.setOnClickListener {
            requestCalendarPermissionsIfNeeded()
        }
        lifecycleScope.launch {
            viewModel.uiState.collect {
                it?.let { state -> bindNewUiState(state) }
            }
        }
    }

    private fun bindNewUiState(state: GCalendarUiState) {
        calendarAdapter.updateItems(state.calendars)
    }

    private fun initCalendarRecyclerView() {
        binding.rvCalendar.itemAnimator = null
        binding.rvCalendar.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCalendar.adapter = calendarAdapter
    }

    private fun arePermissionsGranted(): Boolean {
        val readCalendarPermission = Manifest.permission.READ_CALENDAR
        val writeCalendarPermission = Manifest.permission.WRITE_CALENDAR
        val readPermissionGranted =
            context?.checkSelfPermission(readCalendarPermission) == PackageManager.PERMISSION_GRANTED
        val writePermissionGranted =
            context?.checkSelfPermission(writeCalendarPermission) == PackageManager.PERMISSION_GRANTED
        val arePermissionsGranted = readPermissionGranted && writePermissionGranted
        return arePermissionsGranted
    }

    private fun requestCalendarPermissionsIfNeeded() {
        val readCalendarPermission = Manifest.permission.READ_CALENDAR
        val writeCalendarPermission = Manifest.permission.WRITE_CALENDAR
        if (!arePermissionsGranted()) {
            requestCalendarPermissions.launch(
                arrayOf(
                    readCalendarPermission,
                    writeCalendarPermission
                )
            )
        } else {
            onCalendarPermissionsGranted()
        }
    }

    private fun initNavigation() {
        binding.tbExport.setNavigationOnClickListener {
            (activity as NavigationActivity).openDrawer()
        }
    }

    private fun onCalendarPermissionsGranted() {
        binding.cvGrantPermission.visibility = View.GONE
        binding.btnExport.isEnabled = true
    }

    private fun onCalendarPermissionsDenied() {
        binding.cvGrantPermission.visibility = View.VISIBLE
        binding.btnExport.isEnabled = false
    }

    override fun selectCalendar(calendar: CalendarItem) {
        viewModel.changeSelectedCalendar(calendar)
    }
}