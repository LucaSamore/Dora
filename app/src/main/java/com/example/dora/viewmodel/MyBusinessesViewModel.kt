package com.example.dora.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dora.datastore.UserDatastore
import com.example.dora.di.FirebaseRepository
import com.example.dora.di.IoDispatcher
import com.example.dora.model.Business
import com.example.dora.repository.business.BusinessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MyBusinessesViewModel
@Inject
constructor(
  @FirebaseRepository private val businessRepository: BusinessRepository,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
  private val userDatastore: UserDatastore,
) : ViewModel() {

  private val _myBusinesses = MutableStateFlow(emptyList<Business>())

  val myBusinesses = _myBusinesses.asStateFlow()

  val errorMessage = mutableStateOf("")

  val errorMessageHidden = mutableStateOf(true)
  fun getMyBusinesses() =
    viewModelScope.launch {
      val userId = userDatastore.userId.first() ?: ""

      if (userId.isEmpty()) {
        _myBusinesses.update { emptyList() }
      }

      businessRepository
        .getBusinessesByUserId(userId)
        .fold(
          { error ->
            errorMessage.value = error.message
            errorMessageHidden.value = false
          },
          { businesses -> _myBusinesses.update { businesses } }
        )
    }
}
