package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.common.auth.Credentials
import com.example.dora.repository.auth.AuthenticationRepository
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    // TODO: We don’t want firebase dependencies in viewmodel classes,
    // hence we have to change the either.right return type

    private var _signInResult : Either<ErrorMessage, AuthResult>? = null

    val signInResult
        get() = _signInResult

    fun signIn(emailAddress: String, password: String) = viewModelScope.launch {
        _signInResult = withContext(viewModelScope.coroutineContext) {
            authenticationRepository
                .signInWithEmailAndPassword(Credentials.Login(emailAddress, password))
        }
    }
}