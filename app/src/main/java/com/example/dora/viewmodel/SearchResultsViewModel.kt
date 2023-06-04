package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.di.FirebaseRepository
import com.example.dora.di.IoDispatcher
import com.example.dora.model.Business
import com.example.dora.repository.business.BusinessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltViewModel
class SearchResultsViewModel
@Inject
constructor(
  @FirebaseRepository private val businessRepository: BusinessRepository,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

  suspend fun searchBusinesses(searchKey: String): Either<ErrorMessage, List<Business>> =
    withContext(viewModelScope.coroutineContext + ioDispatcher) {
      businessRepository.getBusinessesByName(searchKey)
    }
}
