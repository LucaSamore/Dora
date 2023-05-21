package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dora.datastore.SettingsDatastore
import com.example.dora.di.FirebaseRepository
import com.example.dora.repository.auth.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    @FirebaseRepository private val authenticationRepository: AuthenticationRepository,
    private val settingsDatastore: SettingsDatastore,
) : ViewModel() {

    val theme = settingsDatastore.theme

    fun saveTheme(theme: String) {
        viewModelScope.launch { settingsDatastore.saveThemeToDataStore(theme) }
    }

    fun signOut() = viewModelScope.launch { authenticationRepository.signOut() }
}
