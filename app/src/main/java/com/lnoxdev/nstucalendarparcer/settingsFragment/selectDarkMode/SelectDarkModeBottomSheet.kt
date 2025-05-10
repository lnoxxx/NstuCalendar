package com.lnoxdev.nstucalendarparcer.settingsFragment.selectDarkMode

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lnoxdev.nstucalendarparcer.R
import com.lnoxdev.nstucalendarparcer.databinding.BottomSheetSelectDarkModeBinding
import com.lnoxdev.nstucalendarparcer.models.UiDarkMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectDarkModeBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetSelectDarkModeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Select dark mode binding null!")

    private val viewModel: SelectDarkModeViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = BottomSheetSelectDarkModeBinding.inflate(LayoutInflater.from(context))
        val dialog =
            BottomSheetDialog(requireContext(), R.style.ThemeOverlay_NstuCalendar_BottomSheet)
        dialog.setContentView(binding.root)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        lifecycleScope.launch {
            viewModel.selectedDarkMode.collect { mode ->
                mode?.let { bindDarkModeState(it) }
            }
        }
        return dialog
    }

    private fun bindDarkModeState(mode: UiDarkMode) {
        val id = when (mode) {
            UiDarkMode.LIGHT -> R.id.rbLight
            UiDarkMode.DARK -> R.id.rbDark
            UiDarkMode.SYSTEM -> R.id.rbSystemDarkMode
        }
        binding.rbgDarkMode.check(id)
        binding.rbgDarkMode.setOnCheckedChangeListener { _, checkedId ->
            val newDarkMode = when (checkedId) {
                R.id.rbLight -> UiDarkMode.LIGHT
                R.id.rbDark -> UiDarkMode.DARK
                R.id.rbSystemDarkMode -> UiDarkMode.SYSTEM
                else -> null
            }
            newDarkMode?.let { changeMode(it) }
        }
    }

    private fun changeMode(mode: UiDarkMode) {
        lifecycleScope.launch {
            val recreate = viewModel.changeDarkMode(mode)
            dialog?.dismiss()
            if (recreate) activity?.recreate()
        }
    }

}