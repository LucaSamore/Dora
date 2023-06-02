package com.example.dora.repository.like

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.dora.common.ErrorMessage
import com.example.dora.model.Like
import com.example.dora.network.NetworkRequest
import com.example.dora.network.database.FirestoreAPI
import com.example.dora.network.database.FirestoreRequest
import com.google.firebase.firestore.Filter
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LikeRepositoryImpl
@Inject
constructor(
  private val firestoreAPI: FirestoreAPI,
  private val ioDispatcher: CoroutineDispatcher,
) : LikeRepository {
  override suspend fun didILikeIt(reviewId: String, userId: String): Either<ErrorMessage, Boolean> =
    withContext(ioDispatcher) {
      try {
        val request =
          FirestoreRequest(
            collection = Like.collection,
            where =
              Filter.and(Filter.equalTo("reviewId", reviewId), Filter.equalTo("userId", userId))
          )

        firestoreAPI
          .findMany(NetworkRequest.of(request))
          .data!!
          .findManyTask!!
          .await()
          .toObjects(Like::class.java)
          .toList()
          .isNotEmpty()
          .right()
      } catch (e: Exception) {
        ErrorMessage(e.message!!).left()
      }
    }

  override suspend fun toggleLike(reviewId: String, userId: String): Either<ErrorMessage, Int> =
    didILikeIt(reviewId, userId)
      .fold(
        { error -> error.left() },
        { right -> if (right) removeLike(reviewId, userId) else likeReview(reviewId, userId) }
      )

  override suspend fun getNumberOfLikes(reviewId: String): Either<ErrorMessage, Int> =
    withContext(ioDispatcher) {
      try {
        val request =
          FirestoreRequest(
            collection = Like.collection,
            where = Filter.equalTo("reviewId", reviewId)
          )

        firestoreAPI
          .findMany(NetworkRequest.of(request))
          .data!!
          .findManyTask!!
          .await()
          .toObjects(Like::class.java)
          .size
          .right()
      } catch (e: Exception) {
        ErrorMessage(e.message!!).left()
      }
    }

  private suspend fun likeReview(reviewId: String, userId: String): Either<ErrorMessage, Int> =
    withContext(ioDispatcher) {
      try {
        val like =
          Like(
            uuid = UUID.randomUUID().toString(),
            userId = userId,
            reviewId = reviewId,
          )
        val request =
          FirestoreRequest(
            data = like,
            collection = Like.collection,
            document = like.uuid,
          )

        firestoreAPI.insert(NetworkRequest.of(request)).data?.insertTask?.await()

        return@withContext getNumberOfLikes(reviewId)
      } catch (e: Exception) {
        ErrorMessage(e.message!!).left()
      }
    }

  private suspend fun removeLike(reviewId: String, userId: String): Either<ErrorMessage, Int> =
    withContext(ioDispatcher) {
      try {
        val request =
          FirestoreRequest(
            collection = Like.collection,
            where =
              Filter.and(Filter.equalTo("reviewId", reviewId), Filter.equalTo("userId", userId))
          )

        val toDelete =
          firestoreAPI
            .findMany(NetworkRequest.of(request))
            .data!!
            .findManyTask!!
            .await()
            .toObjects(Like::class.java)
            .first()

        val deleteRequest = FirestoreRequest(collection = Like.collection, document = toDelete.uuid)

        firestoreAPI
          .deleteSingle(NetworkRequest.of(deleteRequest))
          .data!!
          .deleteSingleTask!!
          .await()

        return@withContext getNumberOfLikes(reviewId)
      } catch (e: Exception) {
        ErrorMessage(e.message!!).left()
      }
    }
}
