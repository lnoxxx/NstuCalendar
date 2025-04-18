package com.lnoxdev.nstucalendarparcer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.lnoxdev.data.appSettings.SettingsManager
import com.lnoxdev.nstucalendarparcer.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activeTheme()
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        initNavigation()
        setContentView(binding.root)
    }

    private fun activeTheme(){
        val settings = runBlocking { settingsManager.settings.first() }
        val theme = if (settings.monetTheme)
            R.style.NstuCalendar_Theme_Monet
        else
            R.style.NstuCalendar_Theme_Default
        setTheme(theme)
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentContainer) as NavHostFragment
        val navBar = binding.bnv as NavigationBarView
        navBar.setupWithNavController(navHostFragment.navController)
    }
}