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
import com.lnoxdev.nstucalendarparcer.NavigationActivity
import com.lnoxdev.nstucalendarparcer.TransitionAnimations
import com.lnoxdev.nstucalendarparcer.databinding.FragmentGCalendarExportBinding
import com.lnoxdev.nstucalendarparcer.exceptions.showErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GCalendarExportFragment : Fragment() {

    private var _binding: FragmentGCalendarExportBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("GCalendarExportFragment binding null!")

    private val viewModel: GCalendarExportViewModel by viewModels()

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
        binding.clMain.setOnApplyWindowInsetsListener { v, insets ->
            val windowInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(bottom = windowInsets.bottom)
            return@setOnApplyWindowInsetsListener insets
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestCalendarPermissionsIfNeeded()
        binding.btnGrandPermission.setOnClickListener {
            requestCalendarPermissionsIfNeeded()
        }
        binding.btnExport.setOnClickListener {
            exportSchedule()
        }
        binding.btnDeleteCalendar.setOnClickListener {
            viewModel.deleteCalendar()
        }
        binding.swcEnableReminder.setOnCheckedChangeListener { _, isChecked ->
            binding.etReminderTime.isEnabled = isChecked
        }
        lifecycleScope.launch {
            viewModel.loadingFlow.collect {
                showLoading(it)
            }
        }
        lifecycleScope.launch {
            viewModel.exceptionFlow.collect { exception ->
                exception?.let { binding.clMain.showErrorSnackBar(it) }
            }
        }
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

    private fun exportSchedule() {
        val reminderTime = if (binding.swcEnableReminder.isChecked)
            binding.etReminderTime.editText?.text.toString().toIntOrNull()
        else null
        viewModel.export(reminderTime)
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.piLoading.show()
            binding.btnExport.isEnabled = false
            binding.btnDeleteCalendar.isEnabled = false
        } else {
            binding.piLoading.hide()
            binding.btnExport.isEnabled = true
            binding.btnDeleteCalendar.isEnabled = true
        }
    }
}