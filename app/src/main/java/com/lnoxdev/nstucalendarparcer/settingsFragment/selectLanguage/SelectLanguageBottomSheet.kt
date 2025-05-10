package com.lnoxdev.nstucalendarparcer.settingsFragment.selectLanguage

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lnoxdev.nstucalendarparcer.R
import com.lnoxdev.nstucalendarparcer.databinding.BottomSheetSelectLanguageBinding
import com.lnoxdev.nstucalendarparcer.models.UiAppLanguage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectLanguageBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetSelectLanguageBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Select language binding null!")

    private val viewModel: SelectLanguageViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = BottomSheetSelectLanguageBinding.inflate(LayoutInflater.from(context))
        val dialog =
            BottomSheetDialog(requireContext(), R.style.ThemeOverlay_NstuCalendar_BottomSheet)
        dialog.setContentView(binding.root)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        lifecycleScope.launch {
            viewModel.selectedLanguage.collect { lang ->
                lang?.let { bindSelectLang(it) }
            }
        }
        return dialog
    }

    private fun bindSelectLang(lang: UiAppLanguage) {
        val id = when (lang) {
            UiAppLanguage.EN -> R.id.rbEn
            UiAppLanguage.RU -> R.id.rbRu
            UiAppLanguage.SYSTEM -> R.id.rbSystem
        }
        binding.rbgLanguage.check(id)
        binding.rbgLanguage.setOnCheckedChangeListener { _, checkedId ->
            val newLang = when (checkedId) {
                R.id.rbEn -> UiAppLanguage.EN
                R.id.rbRu -> UiAppLanguage.RU
                R.id.rbSystem -> UiAppLanguage.SYSTEM
                else -> null
            }
            newLang?.let { viewModel.changeLang(it) }
        }
    }
}