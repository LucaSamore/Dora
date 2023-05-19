package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.common.Location
import com.example.dora.di.FirebaseRepository
import com.example.dora.di.IoDispatcher
import com.example.dora.repository.auth.AuthenticationRepository
import com.example.dora.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    @FirebaseRepository private val authenticationRepository: AuthenticationRepository,
    @FirebaseRepository private val userRepository: UserRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    fun signOut() = viewModelScope.launch { authenticationRepository.signOut() }

    fun updateLocation(location: Location) {
        viewModelScope.launch {
            userRepository.updateLocation(location)
        }
    }

    suspend fun deleteAccount(): Either<ErrorMessage, Void> =
        withContext(viewModelScope.coroutineContext + ioDispatcher) {
            authenticationRepository.deleteUser()
        }
}
