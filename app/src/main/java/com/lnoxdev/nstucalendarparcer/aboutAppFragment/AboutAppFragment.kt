package com.lnoxdev.nstucalendarparcer.aboutAppFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.lnoxdev.nstucalendarparcer.MainActivity
import com.lnoxdev.nstucalendarparcer.databinding.FragmentAboutAppBinding

class AboutAppFragment : Fragment() {

    private var _binding: FragmentAboutAppBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("AboutAppFragment binding null!")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutAppBinding.inflate(inflater)
        initNavigation()
        showVersion()
        return binding.root
    }

    private fun initNavigation() {
        binding.tbAboutApp.setNavigationOnClickListener {
            (requireActivity() as MainActivity).openDrawer()
        }
    }

    private fun showVersion() {
        binding.tvVersionCode.setOnApplyWindowInsetsListener { v, insets ->
            val windowInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(bottom = windowInsets.bottom)
            insets
        }
        val packageInfo =
            requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
        binding.tvVersionCode.text = packageInfo.versionName
    }

}