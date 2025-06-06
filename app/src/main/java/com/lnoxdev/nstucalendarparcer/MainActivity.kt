package com.lnoxdev.nstucalendarparcer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.color.DynamicColors
import com.lnoxdev.data.appSettings.SettingsManager
import com.lnoxdev.nstucalendarparcer.databinding.ActivityMainBinding
import com.lnoxdev.nstucalendarparcer.models.UiDarkMode
import com.lnoxdev.nstucalendarparcer.models.toUiAppTheme
import com.lnoxdev.nstucalendarparcer.models.toUiDarkMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationActivity {

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

    private fun activeTheme() {
        val settings = runBlocking { settingsManager.settings.first() }
        setDarkMode(settings.darkMode.toUiDarkMode())
        setTheme(settings.appTheme.toUiAppTheme().themeRes)
        if (settings.monetTheme)
            DynamicColors.applyToActivityIfAvailable(this)
    }

    private fun setDarkMode(mode: UiDarkMode) {
        val appDelegateMode = when (mode) {
            UiDarkMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            UiDarkMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            UiDarkMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(appDelegateMode)
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentContainer) as NavHostFragment
        binding.drawer.setupWithNavController(navHostFragment.navController)
    }

    override fun openDrawer() {
        binding.drawerLayout?.open()
    }

}