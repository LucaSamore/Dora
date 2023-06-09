package com.example.dora.repository.favorite

import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.database.entity.Favorite
import com.example.dora.model.Business

interface FavoriteRepository {
  suspend fun getFavorites(userId: String): List<Favorite>

  suspend fun insert(favorite: Favorite)

  suspend fun delete(favorite: Favorite)

  suspend fun deleteAll()

  suspend fun exists(businessId: String, userId: String): Boolean

  suspend fun single(businessId: String): Favorite

  suspend fun fetch(vararg businessIds: String): Either<ErrorMessage, List<Business>>
}
