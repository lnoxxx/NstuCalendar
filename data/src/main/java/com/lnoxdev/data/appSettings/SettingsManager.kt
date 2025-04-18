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
        Settings(group, is12TimeFormat, monet)
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

    suspend fun changeMonetThemeAsyncWithResult(enable: Boolean): Boolean{
        return withContext(Dispatchers.IO){
            dataStore.edit { it[MONET_THEME] = if (enable) TRUE else FALSE }
            true
        }
    }

    companion object {
        private val GROUP = stringPreferencesKey("group")
        private val TIME_FORMAT = stringPreferencesKey("time format")
        private val MONET_THEME = stringPreferencesKey("monet theme")
        private const val TRUE = "true"
        private const val FALSE = "false"
    }
}