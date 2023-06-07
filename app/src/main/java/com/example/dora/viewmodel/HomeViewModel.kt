package com.example.dora.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dora.common.Location
import com.example.dora.di.FirebaseRepository
import com.example.dora.model.Business
import com.example.dora.repository.business.BusinessRepository
import com.example.dora.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel
@Inject
constructor(
  @FirebaseRepository private val userRepository: UserRepository,
  @FirebaseRepository private val businessRepository: BusinessRepository,
) : ViewModel() {

  private val _businesses = MutableStateFlow(emptyList<Business>())

  val businesses = _businesses.asStateFlow()

  val errorMessage = mutableStateOf("")

  val errorMessageHidden = mutableStateOf(true)

  fun updateLocation(location: Location) {
    viewModelScope.launch { userRepository.updateLocation(location) }
  }

  fun getBusinessesClosedToMe(location: Location) =
    viewModelScope.launch {
      businessRepository
        .getBusinessesClosedToMe(location)
        .fold(
          { error ->
            errorMessage.value = error.message
            errorMessageHidden.value = false
          },
          { closedBusinesses -> _businesses.update { closedBusinesses } }
        )
    }

  fun getBusinessesDefault() =
    viewModelScope.launch {
      businessRepository
        .getBusinessesDefault()
        .fold(
          { error ->
            errorMessage.value = error.message
            errorMessageHidden.value = false
          },
          { defaultBusinesses -> _businesses.update { defaultBusinesses } }
        )
    }
}
