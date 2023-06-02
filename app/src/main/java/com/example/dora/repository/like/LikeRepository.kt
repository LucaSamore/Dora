package com.example.dora.repository.like

import arrow.core.Either
import com.example.dora.common.ErrorMessage

interface LikeRepository {
  suspend fun didILikeIt(reviewId: String, userId: String): Either<ErrorMessage, Boolean>

  suspend fun toggleLike(reviewId: String, userId: String): Either<ErrorMessage, Int>

  suspend fun getNumberOfLikes(reviewId: String): Either<ErrorMessage, Int>
}
