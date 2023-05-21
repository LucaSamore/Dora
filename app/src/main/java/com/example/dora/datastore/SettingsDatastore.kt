package com.example.dora.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.dora.R
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SettingsDatastore(private val context: Context) {

    val theme: Flow<String> =
        context.datastore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map { preferences -> preferences[UI_THEME] ?: context.getString(R.string.light_theme) }

    suspend fun saveThemeToDataStore(theme: String) {
        context.datastore.edit { preferences -> preferences[UI_THEME] = theme }
    }

    companion object {
        private val Context.datastore: DataStore<Preferences> by
            preferencesDataStore(name = "settings_preferences")
        private val UI_THEME = stringPreferencesKey(name = "ui_theme")
    }
}
