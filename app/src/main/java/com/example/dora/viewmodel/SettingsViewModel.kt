package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.common.SuccessMessage
import com.example.dora.datastore.SettingsDatastore
import com.example.dora.di.FirebaseRepository
import com.example.dora.di.IoDispatcher
import com.example.dora.repository.auth.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SettingsViewModel
@Inject
constructor(
    @FirebaseRepository private val authenticationRepository: AuthenticationRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val settingsDatastore: SettingsDatastore,
) : ViewModel() {

    val theme = settingsDatastore.theme

    fun saveTheme(theme: String) {
        viewModelScope.launch { settingsDatastore.saveThemeToDataStore(theme) }
    }

    fun signOut() = viewModelScope.launch { authenticationRepository.signOut() }

    suspend fun deleteAccount(): Either<ErrorMessage, SuccessMessage> =
        withContext(viewModelScope.coroutineContext + ioDispatcher) {
            authenticationRepository.deleteUser()
        }
}
