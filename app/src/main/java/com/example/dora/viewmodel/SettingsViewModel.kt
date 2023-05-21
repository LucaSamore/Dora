package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dora.datastore.SettingsDatastore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    private val settingsDatastore: SettingsDatastore,
) : ViewModel() {

    val theme = settingsDatastore.theme

    fun saveTheme(theme: String) {
        viewModelScope.launch { settingsDatastore.saveThemeToDataStore(theme) }
    }
}
