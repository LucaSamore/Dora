package com.example.dora.repository.review

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.dora.common.ErrorMessage
import com.example.dora.common.SuccessMessage
import com.example.dora.model.Review
import com.example.dora.network.NetworkRequest
import com.example.dora.network.database.FirestoreAPI
import com.example.dora.network.database.FirestoreRequest
import com.google.firebase.firestore.Filter
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ReviewRepositoryImpl
@Inject
constructor(
  private val firestoreAPI: FirestoreAPI,
  private val ioDispatcher: CoroutineDispatcher,
) : ReviewRepository {
  override suspend fun insertReview(review: Review): Either<ErrorMessage, SuccessMessage> =
    withContext(ioDispatcher) {
      try {
        val request =
          FirestoreRequest(data = review, collection = Review.collection, document = review.uuid)

        firestoreAPI.insert(NetworkRequest.of(request)).data?.insertTask?.await().let {
          SuccessMessage("Review added successfully").right()
        }
      } catch (e: Exception) {
        ErrorMessage(e.message!!).left()
      }
    }

  override suspend fun getReviewsByBusinessId(
    businessId: String
  ): Either<ErrorMessage, List<Review>> =
    withContext(ioDispatcher) {
      try {
        val request =
          FirestoreRequest(
            collection = Review.collection,
            where = Filter.equalTo("businessId", businessId)
          )

        firestoreAPI
          .find(NetworkRequest.of(request))
          .data!!
          .findTask!!
          .await()
          .toObjects(Review::class.java)
          .toList()
          .right()
      } catch (e: Exception) {
        ErrorMessage(e.message!!).left()
      }
    }
}
