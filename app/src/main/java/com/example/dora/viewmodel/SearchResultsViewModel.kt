package com.example.dora.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dora.di.FirebaseRepository
import com.example.dora.model.Business
import com.example.dora.repository.business.BusinessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SearchResultsViewModel
@Inject
constructor(
  @FirebaseRepository private val businessRepository: BusinessRepository,
) : ViewModel() {

  private val _searchResult = MutableStateFlow(emptyList<Business>())

  val searchResult = _searchResult.asStateFlow()

  val errorMessage = mutableStateOf("")

  val errorMessageHidden = mutableStateOf(true)

  fun searchBusinesses(searchKey: String) =
    viewModelScope.launch {
      businessRepository
        .getBusinessesByName(searchKey)
        .fold(
          { error ->
            errorMessage.value = error.message
            errorMessageHidden.value = false
          },
          { businesses -> _searchResult.update { businesses } }
        )
    }
}
