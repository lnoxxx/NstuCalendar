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
            else -> AppTheme.NSTU
        }
        Settings(group, is12TimeFormat, monet, appTheme)
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
        private const val TRUE = "true"
        private const val FALSE = "false"

        enum class AppTheme(val saveKey: String) {
            NSTU("NstuTheme"),
            CORNFLOWER("CornflowerTheme"),
            DARK_ORCHID("DarkOrchid")
        }
    }
}