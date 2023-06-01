package com.example.dora.repository.vote

import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.model.Vote

interface VoteRepository {
  suspend fun getVotes(reviewId: String): Either<ErrorMessage, Int>

  suspend fun insertVote(vote: Vote): Either<ErrorMessage, Int>

  suspend fun deleteVote(reviewId: String, userId: String): Either<ErrorMessage, Int>
}
