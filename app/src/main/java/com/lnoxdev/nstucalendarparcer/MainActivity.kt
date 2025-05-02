package com.lnoxdev.nstucalendarparcer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.color.DynamicColors
import com.lnoxdev.data.appSettings.SettingsManager
import com.lnoxdev.nstucalendarparcer.databinding.ActivityMainBinding
import com.lnoxdev.nstucalendarparcer.models.toUiAppTheme
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
        setContentView(binding.root)
    }

    private fun activeTheme() {
        val settings = runBlocking { settingsManager.settings.first() }
        setTheme(settings.appTheme.toUiAppTheme().themeRes)
        if (settings.monetTheme)
            DynamicColors.applyToActivityIfAvailable(this)
    }
}