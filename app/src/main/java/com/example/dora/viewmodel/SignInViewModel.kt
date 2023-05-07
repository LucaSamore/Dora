package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.common.auth.Credentials
import com.example.dora.repository.auth.AuthenticationRepository
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {
    suspend fun signIn(emailAddress: String, password: String): Either<ErrorMessage, AuthResult> =
        withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            authenticationRepository
                .signInWithEmailAndPassword(Credentials.Login(emailAddress, password))
        }
}