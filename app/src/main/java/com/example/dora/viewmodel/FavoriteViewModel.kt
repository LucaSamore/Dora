package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.right
import com.example.dora.common.ErrorMessage
import com.example.dora.database.entity.Favorite
import com.example.dora.di.IoDispatcher
import com.example.dora.model.Business
import com.example.dora.repository.favorite.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class FavoriteViewModel
@Inject
constructor(
  private val favoriteRepository: FavoriteRepository,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

  fun add(favorite: Favorite) = viewModelScope.launch { favoriteRepository.insert(favorite) }

  fun deleteOne(favorite: Favorite) = viewModelScope.launch { favoriteRepository.delete(favorite) }

  fun deleteAll() = viewModelScope.launch { favoriteRepository.deleteAll() }

  suspend fun getBusinesses(): Either<ErrorMessage, List<Business>> =
    withContext(viewModelScope.coroutineContext + ioDispatcher) {
      val ids = favoriteRepository.getFavorites().map { it.businessId }.toTypedArray()

      if (ids.isEmpty()) {
        return@withContext listOf<Business>().right()
      }

      return@withContext favoriteRepository.fetch(*ids)
    }
}
