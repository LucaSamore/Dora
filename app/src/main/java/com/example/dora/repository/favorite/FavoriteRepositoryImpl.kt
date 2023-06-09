package com.example.dora.repository.favorite

import androidx.annotation.WorkerThread
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.dora.common.ErrorMessage
import com.example.dora.database.dao.FavoriteDAO
import com.example.dora.database.entity.Favorite
import com.example.dora.model.Business
import com.example.dora.network.NetworkRequest
import com.example.dora.network.database.FirestoreAPI
import com.example.dora.network.database.FirestoreRequest
import com.google.firebase.firestore.Filter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FavoriteRepositoryImpl(
  private val favoriteDAO: FavoriteDAO,
  private val firestoreAPI: FirestoreAPI,
  private val ioDispatcher: CoroutineDispatcher,
) : FavoriteRepository {
  override suspend fun getFavorites(userId: String): List<Favorite> =
    favoriteDAO.getFavorites(userId)

  @WorkerThread
  override suspend fun insert(favorite: Favorite) {
    favoriteDAO.insert(favorite)
  }

  @WorkerThread
  override suspend fun delete(favorite: Favorite) {
    favoriteDAO.delete(favorite)
  }

  @WorkerThread
  override suspend fun deleteAll() {
    favoriteDAO.deleteAll()
  }

  @WorkerThread override suspend fun exists(businessId: String) = favoriteDAO.exists(businessId)

  @WorkerThread override suspend fun single(businessId: String) = favoriteDAO.single(businessId)

  override suspend fun fetch(vararg businessIds: String): Either<ErrorMessage, List<Business>> =
    withContext(ioDispatcher) {
      try {
        if (businessIds.isEmpty()) {
          return@withContext emptyList<Business>().right()
        }

        val request =
          FirestoreRequest(
            collection = Business.collection,
            where = Filter.inArray("uuid", businessIds.toList())
          )

        firestoreAPI
          .findMany(NetworkRequest.of(request))
          .data!!
          .findManyTask!!
          .await()
          .toObjects(Business::class.java)
          .toList()
          .right()
      } catch (e: Exception) {
        ErrorMessage(e.message!!).left()
      }
    }
}
