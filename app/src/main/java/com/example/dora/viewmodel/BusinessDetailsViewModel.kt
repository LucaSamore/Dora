package com.example.dora.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import arrow.core.left
import com.example.dora.common.ErrorMessage
import com.example.dora.database.entity.Favorite
import com.example.dora.datastore.UserDatastore
import com.example.dora.di.FirebaseRepository
import com.example.dora.di.IoDispatcher
import com.example.dora.model.Business
import com.example.dora.model.Review
import com.example.dora.repository.business.BusinessRepository
import com.example.dora.repository.favorite.FavoriteRepository
import com.example.dora.repository.like.LikeRepository
import com.example.dora.repository.review.ReviewRepository
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
  @FirebaseRepository private val reviewRepository: ReviewRepository,
  @FirebaseRepository private val favoriteRepository: FavoriteRepository,
  @FirebaseRepository private val likeRepository: LikeRepository,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
  private val userDatastore: UserDatastore,
) : ViewModel() {

  private val _business = MutableStateFlow<Business?>(null)

  private val _reviews = MutableStateFlow(emptyList<Review>())

  val business = _business.asStateFlow()

  val reviews = _reviews.asStateFlow()

  var favoriteIconFilled = mutableStateOf(false)

  val errorMessage = mutableStateOf("")

  val errorMessageHidden = mutableStateOf(true)

  fun getBusiness(businessId: String) =
    viewModelScope.launch {
      businessRepository
        .getBusinessById(businessId)
        .fold(
          { error ->
            errorMessage.value = error.message
            errorMessageHidden.value = false
          },
          { business -> _business.update { business } }
        )
    }

  fun getReviews(businessId: String) =
    viewModelScope.launch {
      reviewRepository
        .getReviewsByBusinessId(businessId)
        .fold(
          { error ->
            errorMessage.value = error.message
            errorMessageHidden.value = false
          },
          { reviews -> _reviews.update { reviews } }
        )
    }

  suspend fun didILikeIt(reviewId: String): Either<ErrorMessage, Boolean> =
    withContext(viewModelScope.coroutineContext + ioDispatcher) {
      val userId = userDatastore.userId.first()

      if (userId.isNullOrEmpty()) {
        return@withContext ErrorMessage("User id is null").left()
      }

      likeRepository.didILikeIt(reviewId, userId)
    }

  suspend fun getReviewNumberOfLikes(reviewId: String): Either<ErrorMessage, Int> =
    withContext(viewModelScope.coroutineContext + ioDispatcher) {
      likeRepository.getNumberOfLikes(reviewId)
    }

  suspend fun toggleReviewLike(reviewId: String): Either<ErrorMessage, Int> =
    withContext(viewModelScope.coroutineContext + ioDispatcher) {
      val userId = userDatastore.userId.first()

      if (userId.isNullOrEmpty()) {
        return@withContext ErrorMessage("User id is null").left()
      }

      likeRepository.toggleLike(reviewId, userId)
    }

  fun isInFavorites(businessId: String) =
    viewModelScope.launch { favoriteIconFilled.value = favoriteRepository.exists(businessId) }

  fun toggleFavorite(businessId: String) =
    viewModelScope.launch {
      val userId = userDatastore.userId.first() ?: ""
      if (favoriteRepository.exists(businessId)) {
        favoriteRepository.delete(favoriteRepository.single(businessId))
      } else {
        favoriteRepository.insert(
          Favorite(uuid = UUID.randomUUID().toString(), businessId = businessId, userId = userId)
        )
      }
    }
}
