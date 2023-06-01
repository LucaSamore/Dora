package com.example.dora.viewmodel

import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.left
import com.example.dora.common.ErrorMessage
import com.example.dora.common.SuccessMessage
import com.example.dora.di.FirebaseRepository
import com.example.dora.di.IoDispatcher
import com.example.dora.model.Review
import com.example.dora.repository.review.ReviewRepository
import com.example.dora.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltViewModel
class WriteReviewViewModel
@Inject
constructor(
  @FirebaseRepository private val reviewRepository: ReviewRepository,
  @FirebaseRepository private val userRepository: UserRepository,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

  suspend fun addReview(
    businessId: String,
    content: String,
    rating: Int
  ): Either<ErrorMessage, SuccessMessage> =
    when (val user = userRepository.getUser()) {
      is Either.Left -> user.value.left()
      is Either.Right -> {
        val review =
          Review(
            uuid = UUID.randomUUID().toString(),
            content = content,
            rating = rating,
            user = user.value,
            businessId = businessId,
          )
        storeReview(review)
      }
    }

  private suspend fun storeReview(review: Review) =
    withContext(ioDispatcher) { reviewRepository.insertReview(review) }
}
