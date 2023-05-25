package com.example.dora.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.right
import com.example.dora.common.ErrorMessage
import com.example.dora.common.SuccessMessage
import com.example.dora.di.FirebaseRepository
import com.example.dora.di.IoDispatcher
import com.example.dora.model.User
import com.example.dora.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    @FirebaseRepository private val userRepository: UserRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _user = MutableStateFlow<Either<ErrorMessage, User>>(User().right())

    val user = _user.asStateFlow()

    val progressIndicatorHidden = mutableStateOf(true)

    init {
        viewModelScope.launch(ioDispatcher) { _user.update { userRepository.getUser() } }
    }

    suspend fun updateProfile(user: User): Either<ErrorMessage, SuccessMessage> =
        withContext(viewModelScope.coroutineContext + ioDispatcher) {
            userRepository.updateUser(user)
        }

    suspend fun updateProfilePicture(userId: String, profilePictureUri: Uri) =
        withContext(viewModelScope.coroutineContext + ioDispatcher) {
            userRepository.updateProfilePicture(userId, profilePictureUri)
        }
}
