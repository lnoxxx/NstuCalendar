package com.lnoxdev.data.appSettings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val dataStore: DataStore<Preferences>) {

    val group: Flow<String?> = dataStore.data.map { it[GROUP] }

    fun changeGroup(group: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { it[GROUP] = group }
        }
    }

    companion object {
        private val GROUP = stringPreferencesKey("group")
    }
}