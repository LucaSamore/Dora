package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dora.datastore.UserDatastore
import com.example.dora.di.FirebaseRepository
import com.example.dora.di.IoDispatcher
import com.example.dora.repository.business.BusinessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@HiltViewModel
class MyBusinessesViewModel
@Inject
constructor(
  @FirebaseRepository private val businessRepository: BusinessRepository,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
  private val userDatastore: UserDatastore,
) : ViewModel() {

  suspend fun getMyBusinesses() =
    withContext(viewModelScope.coroutineContext + ioDispatcher) {
      businessRepository.getBusinessesByUserId(userDatastore.userId.first()!!)
    }
}
