package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.common.Location
import com.example.dora.di.FirebaseRepository
import com.example.dora.di.IoDispatcher
import com.example.dora.model.Business
import com.example.dora.repository.business.BusinessRepository
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
  @FirebaseRepository private val userRepository: UserRepository,
  @FirebaseRepository private val businessRepository: BusinessRepository,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

  fun updateLocation(location: Location) {
    viewModelScope.launch { userRepository.updateLocation(location) }
  }

  suspend fun getBusinessesClosedToMe(location: Location): Either<ErrorMessage, List<Business>> =
    withContext(viewModelScope.coroutineContext + ioDispatcher) {
      businessRepository.getBusinessesClosedToMe(location)
    }

  suspend fun getBusinessesDefault(): Either<ErrorMessage, List<Business>> =
    withContext(viewModelScope.coroutineContext + ioDispatcher) {
      businessRepository.getBusinessesDefault()
    }
}
