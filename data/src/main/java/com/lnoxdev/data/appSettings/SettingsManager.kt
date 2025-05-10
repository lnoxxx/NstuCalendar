package com.lnoxdev.data.appSettings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lnoxdev.data.models.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager(private val dataStore: DataStore<Preferences>) {

    val settings: Flow<Settings> = dataStore.data.map {
        val group = it[GROUP]
        val is12TimeFormat = it[TIME_FORMAT] == TRUE
        val monet = it[MONET_THEME] == TRUE
        val appTheme = when (it[APP_THEME]) {
            AppTheme.NSTU.saveKey -> AppTheme.NSTU
            AppTheme.CORNFLOWER.saveKey -> AppTheme.CORNFLOWER
            AppTheme.DARK_ORCHID.saveKey -> AppTheme.DARK_ORCHID
            AppTheme.MOUNTAIN_MEADOW.saveKey -> AppTheme.MOUNTAIN_MEADOW
            AppTheme.PEAR.saveKey -> AppTheme.PEAR
            AppTheme.SALMON_ORANGE.saveKey -> AppTheme.SALMON_ORANGE
            AppTheme.TELEMAGENTA.saveKey -> AppTheme.TELEMAGENTA
            AppTheme.TOAD_IN_LOVE.saveKey -> AppTheme.TOAD_IN_LOVE
            AppTheme.TURQUOISE.saveKey -> AppTheme.TURQUOISE
            else -> AppTheme.NSTU
        }
        val darkMode = when (it[DARK_MODE]) {
            AppDarkMode.DARK.saveKey -> AppDarkMode.DARK
            AppDarkMode.LIGHT.saveKey -> AppDarkMode.LIGHT
            AppDarkMode.SYSTEM.saveKey -> AppDarkMode.SYSTEM
            else -> AppDarkMode.SYSTEM
        }
        val language = when (it[LANGUAGE]) {
            AppLanguage.EN.saveKey -> AppLanguage.EN
            AppLanguage.RU.saveKey -> AppLanguage.RU
            AppLanguage.SYSTEM.saveKey -> AppLanguage.SYSTEM
            else -> AppLanguage.SYSTEM
        }
        Settings(group, is12TimeFormat, monet, appTheme, darkMode, language)
    }

    fun changeGroup(group: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { it[GROUP] = group }
        }
    }

    fun changeTimeFormat(is12TimeFormat: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit {
                it[TIME_FORMAT] = if (is12TimeFormat) TRUE else FALSE
            }
        }
    }

    fun changeDarkMode(darkMode: AppDarkMode) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit {
                it[DARK_MODE] = darkMode.saveKey
            }
        }
    }

    fun changeLanguage(language: AppLanguage) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit {
                it[LANGUAGE] = language.saveKey
            }
        }
    }

    suspend fun changeMonetThemeAsyncWithResult(enable: Boolean): Boolean {
        return withContext(Dispatchers.IO) {
            dataStore.edit { it[MONET_THEME] = if (enable) TRUE else FALSE }
            true
        }
    }

    suspend fun changeAppThemeAsyncWithResult(theme: AppTheme): Boolean {
        return withContext(Dispatchers.IO) {
            dataStore.edit { it[APP_THEME] = theme.saveKey }
            true
        }
    }

    companion object {
        private val GROUP = stringPreferencesKey("group")
        private val TIME_FORMAT = stringPreferencesKey("time format")
        private val MONET_THEME = stringPreferencesKey("monet theme")
        private val APP_THEME = stringPreferencesKey("app theme")
        private val LANGUAGE = stringPreferencesKey("language")
        private val DARK_MODE = stringPreferencesKey("dark mode")

        private const val TRUE = "true"
        private const val FALSE = "false"

        enum class AppDarkMode(val saveKey: String) {
            DARK("DARK"),
            LIGHT("LIGHT"),
            SYSTEM("SYSTEM")
        }

        enum class AppLanguage(val saveKey: String) {
            EN("ENGLISH"),
            RU("RUSSIAN"),
            SYSTEM("SYSTEM")
        }

        enum class AppTheme(val saveKey: String) {
            NSTU("NSTU"),
            CORNFLOWER("CORNFLOWER"),
            DARK_ORCHID("DARK_ORCHID"),
            MOUNTAIN_MEADOW("MOUNTAIN_MEADOW"),
            PEAR("PEAR"),
            SALMON_ORANGE("SALMON_ORANGE"),
            TELEMAGENTA("TELEMAGENTA"),
            TOAD_IN_LOVE("TOAD_IN_LOVE"),
            TURQUOISE("TURQUOISE"),
        }
    }
}