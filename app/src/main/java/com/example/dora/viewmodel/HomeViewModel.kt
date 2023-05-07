package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.repository.auth.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    fun signOut() = viewModelScope.launch { authenticationRepository.signOut() }

    suspend fun deleteAccount(): Either<ErrorMessage, Void> =
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            authenticationRepository.deleteUser()
        }
}