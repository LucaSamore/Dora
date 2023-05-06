package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.dora.common.auth.Credentials
import com.example.dora.repository.auth.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    fun signIn(emailAddress: String, password: String) : Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            val signInResult = authenticationRepository
                .signInWithEmailAndPassword(Credentials.Login(emailAddress, password))

            when (signInResult.single()) {
                is Either.Left -> false
                is Either.Right -> true
            }
        }
        return true
    }

}