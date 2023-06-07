package com.example.dora.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dora.database.entity.Favorite
import com.example.dora.datastore.UserDatastore
import com.example.dora.di.FirebaseRepository
import com.example.dora.model.Business
import com.example.dora.repository.favorite.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class FavoriteViewModel
@Inject
constructor(
  @FirebaseRepository private val favoriteRepository: FavoriteRepository,
  private val userDatastore: UserDatastore,
) : ViewModel() {

  private val _favorites = MutableStateFlow(emptyList<Business>())

  val favorites = _favorites.asStateFlow()

  val errorMessage = mutableStateOf("")

  val errorMessageHidden = mutableStateOf(true)

  fun add(favorite: Favorite) = viewModelScope.launch { favoriteRepository.insert(favorite) }

  fun deleteOne(favorite: Favorite) = viewModelScope.launch { favoriteRepository.delete(favorite) }

  fun deleteAll() = viewModelScope.launch { favoriteRepository.deleteAll() }

  fun getBusinesses() =
    viewModelScope.launch {
      val userId = userDatastore.userId.first() ?: ""

      if (userId.isEmpty()) {
        _favorites.update { emptyList() }
      }

      val ids = favoriteRepository.getFavorites(userId).map { it.businessId }.toTypedArray()

      if (ids.isEmpty()) {
        _favorites.update { emptyList() }
      }

      favoriteRepository
        .fetch(*ids)
        .fold(
          { error ->
            errorMessage.value = error.message
            errorMessageHidden.value = false
          },
          { businesses -> _favorites.update { businesses } }
        )
    }
}
