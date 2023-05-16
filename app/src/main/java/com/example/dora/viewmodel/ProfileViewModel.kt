package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.di.FirebaseRepository
import com.example.dora.di.IoDispatcher
import com.example.dora.model.User
import com.example.dora.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    @FirebaseRepository private val userRepository: UserRepository,
) : ViewModel() {

    val user: Flow<Either<ErrorMessage, User>> = flow {
        emit(userRepository.getUser())
    }
}