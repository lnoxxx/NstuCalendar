package com.lnoxdev.nstucalendarparcer.settingsFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.shape.MaterialShapeDrawable
import com.lnoxdev.nstucalendarparcer.R
import com.lnoxdev.nstucalendarparcer.databinding.FragmentSettingsBinding
import com.lnoxdev.nstucalendarparcer.models.SettingsUiState
import com.lnoxdev.nstucalendarparcer.models.UiAppTheme
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.SettingsRecyclerViewAdapter
import com.lnoxdev.nstucalendarparcer.settingsFragment.settingsRecyclerView.SettingsRecyclerViewItemDecorator
import com.lnoxdev.nstucalendarparcer.utils.getThemeColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment(), SettingsRecyclerViewAdapter.SettingsRecyclerViewListener {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Settings binding null!")

    private var _adapter: SettingsRecyclerViewAdapter? = null
    private val adapter get() = _adapter ?: throw IllegalStateException("Settings adapter null!")

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater)
        _adapter = SettingsRecyclerViewAdapter(this)
        binding.ablSettings.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(context)
        binding.ablSettings
            .setStatusBarForegroundColor(
                requireContext()
                    .getThemeColor(com.google.android.material.R.attr.colorSurfaceContainer)
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        lifecycleScope.launch {
            viewModel.uiState.collect { it?.let { bindUiState(it) } }
        }
    }

    private fun initRecyclerView() {
        binding.rvSettings.adapter = adapter
        binding.rvSettings.layoutManager = LinearLayoutManager(context)
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        divider.isLastItemDecorated = false
        binding.rvSettings.addItemDecoration(divider)
        val margin = resources.getDimension(R.dimen.settings_margin).toInt()
        binding.rvSettings.addItemDecoration(SettingsRecyclerViewItemDecorator(margin))
    }

    private fun bindUiState(uiState: SettingsUiState) {
        adapter.updateSettings(uiState)
    }

    override fun onChangeTimeFormat(is12TimeFormat: Boolean) {
        viewModel.changeTimeFormat(is12TimeFormat)
    }

    override fun onChangeGroup() {
        findNavController().navigate(R.id.selectGroupBottomSheet)
    }

    override fun onChangeMonetTheme(enable: Boolean) {
        lifecycleScope.launch {
            val recreate = viewModel.changeMonetTheme(enable)
            if (recreate) activity?.recreate()
        }
    }

    override fun onChangeTheme(theme: UiAppTheme) {
        lifecycleScope.launch {
            val recreate = viewModel.changeTheme(theme)
            if (recreate) activity?.recreate()
        }
    }
}