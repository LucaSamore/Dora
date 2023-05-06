package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dora.common.auth.Credentials
import com.example.dora.repository.auth.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {
    suspend fun signIn(emailAddress: String, password: String) =
        withContext(viewModelScope.coroutineContext) {
            authenticationRepository
                .signInWithEmailAndPassword(Credentials.Login(emailAddress, password))
        }
}