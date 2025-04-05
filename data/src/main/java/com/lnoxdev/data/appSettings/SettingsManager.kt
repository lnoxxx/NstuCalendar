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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager(private val dataStore: DataStore<Preferences>) {
    val settings: Flow<Settings> = dataStore.data.map {
        val group = it[GROUP]
        val timeFormat = it[TIME_FORMAT]
        val is12TimeFormat = timeFormat == TIME_FORMAT_12
        Settings(group, is12TimeFormat)
    }

    fun changeGroup(group: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { it[GROUP] = group }
        }
    }

    fun changeTimeFormat(is12TimeFormat: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit {
                it[TIME_FORMAT] = if (is12TimeFormat) TIME_FORMAT_12 else TIME_FORMAT_24
            }
        }
    }

    companion object {
        private val GROUP = stringPreferencesKey("group")
        private val TIME_FORMAT = stringPreferencesKey("time format")
        private const val TIME_FORMAT_12 = "12HOURS"
        private const val TIME_FORMAT_24 = "24HOURS"
    }
}