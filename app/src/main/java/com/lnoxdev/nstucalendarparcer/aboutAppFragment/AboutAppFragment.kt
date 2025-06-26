package com.lnoxdev.nstucalendarparcer.aboutAppFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.lnoxdev.nstucalendarparcer.MainActivity
import com.lnoxdev.nstucalendarparcer.databinding.FragmentAboutAppBinding
import androidx.core.net.toUri
import com.lnoxdev.nstucalendarparcer.R

class AboutAppFragment : Fragment() {

    private val githubURL = "https://github.com/lnoxxx/NstuCalendar"
    private val rustoreURL = "https://www.rustore.ru/catalog/app/com.lnoxdev.nstucalendarparcer"

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cvGithub.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = githubURL.toUri()
            try {
                startActivity(intent)
            }catch (e: Exception){
                Toast.makeText(context, R.string.failed_open_link, Toast.LENGTH_SHORT).show()
            }
        }
        binding.cvRustore.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = rustoreURL.toUri()
            try {
                startActivity(intent)
            }catch (e: Exception){
                Toast.makeText(context, R.string.failed_open_link, Toast.LENGTH_SHORT).show()
            }
        }
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