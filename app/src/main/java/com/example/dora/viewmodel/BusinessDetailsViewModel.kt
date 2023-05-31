package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.database.entity.Favorite
import com.example.dora.datastore.UserDatastore
import com.example.dora.di.FirebaseRepository
import com.example.dora.di.IoDispatcher
import com.example.dora.model.Business
import com.example.dora.repository.business.BusinessRepository
import com.example.dora.repository.favorite.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class BusinessDetailsViewModel
@Inject
constructor(
    @FirebaseRepository private val businessRepository: BusinessRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val favoriteRepository: FavoriteRepository,
    private val userDatastore: UserDatastore,
) : ViewModel() {
    suspend fun getBusiness(businessId: String): Either<ErrorMessage, Business> =
        withContext(viewModelScope.coroutineContext + ioDispatcher) {
            businessRepository.getBusinessById(businessId)
        }

    suspend fun isInFavorites(businessId: String): Boolean =
        withContext(viewModelScope.coroutineContext + ioDispatcher) {
            favoriteRepository.exists(businessId)
        }

    fun toggleFavorite(businessId: String) =
        viewModelScope.launch {
            val userId = userDatastore.userId.first() ?: ""
            if (favoriteRepository.exists(businessId)) {
                favoriteRepository.delete(favoriteRepository.single(businessId))
            } else {
                favoriteRepository.insert(
                    Favorite(
                        uuid = UUID.randomUUID().toString(),
                        businessId = businessId,
                        userId = userId
                    )
                )
            }
        }
}
