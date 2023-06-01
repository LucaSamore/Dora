package com.example.dora.repository.vote

import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.model.Vote
import com.example.dora.network.database.FirestoreAPI
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

class VoteRepositoryImpl
@Inject
constructor(
  private val firestoreAPI: FirestoreAPI,
  private val ioDispatcher: CoroutineDispatcher,
) : VoteRepository {

  override suspend fun getVotes(reviewId: String): Either<ErrorMessage, Int> {
    TODO("Not yet implemented")
  }

  override suspend fun insertVote(vote: Vote): Either<ErrorMessage, Int> {
    TODO("Not yet implemented")
  }

  override suspend fun deleteVote(reviewId: String, userId: String): Either<ErrorMessage, Int> {
    TODO("Not yet implemented")
  }
}
