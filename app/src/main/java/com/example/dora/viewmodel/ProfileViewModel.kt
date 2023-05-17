package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.right
import com.example.dora.common.ErrorMessage
import com.example.dora.di.FirebaseRepository
import com.example.dora.di.MainDispatcher
import com.example.dora.model.User
import com.example.dora.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    @FirebaseRepository private val userRepository: UserRepository,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _user = MutableStateFlow<Either<ErrorMessage, User?>>(User().right())

    val user = _user.asStateFlow()

    init {
        viewModelScope.launch(mainDispatcher) {
            _user.update {
                userRepository.getUser()
            }
        }
    }
}
