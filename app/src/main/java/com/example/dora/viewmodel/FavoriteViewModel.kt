package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dora.database.entity.Favorite
import com.example.dora.repository.favorite.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val favoriteRepository: FavoriteRepository) :
    ViewModel() {

    val favorites = favoriteRepository.getFavorites()

    fun add(favorite: Favorite) = viewModelScope.launch { favoriteRepository.insert(favorite) }

    fun deleteOne(favorite: Favorite) =
        viewModelScope.launch { favoriteRepository.delete(favorite) }

    fun deleteAll() = viewModelScope.launch { favoriteRepository.deleteAll() }
}
