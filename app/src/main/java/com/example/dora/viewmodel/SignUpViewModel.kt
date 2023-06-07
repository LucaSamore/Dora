package com.example.dora.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.common.auth.Credentials
import com.example.dora.common.auth.SignedUser
import com.example.dora.di.FirebaseRepository
import com.example.dora.di.IoDispatcher
import com.example.dora.repository.auth.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltViewModel
class SignUpViewModel
@Inject
constructor(
  @FirebaseRepository private val authenticationRepository: AuthenticationRepository,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

  suspend fun signUp(
    firstName: String,
    lastName: String,
    emailAddress: String,
    password: String,
    profilePicture: Uri,
  ): Either<ErrorMessage, SignedUser> =
    withContext(viewModelScope.coroutineContext + ioDispatcher) {
      authenticationRepository.signUpWithEmailAndPassword(
        Credentials.Register(
          firstName = firstName,
          lastName = lastName,
          emailAddress = emailAddress,
          password = password,
          profilePicture = profilePicture,
        )
      )
    }
}
