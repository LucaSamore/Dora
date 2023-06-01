package com.example.dora.repository.review

import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.common.SuccessMessage
import com.example.dora.model.Review

interface ReviewRepository {
  suspend fun insertReview(review: Review): Either<ErrorMessage, SuccessMessage>

  suspend fun getReviewsByBusinessId(businessId: String): Either<ErrorMessage, List<Review>>
}
