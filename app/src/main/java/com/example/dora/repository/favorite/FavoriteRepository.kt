package com.example.dora.repository.favorite

import com.example.dora.database.entity.Favorite
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavorites(): Flow<List<Favorite>>

    suspend fun insert(favorite: Favorite)

    suspend fun delete(favorite: Favorite)

    suspend fun deleteAll()

    suspend fun exists(businessId: String): Boolean

    suspend fun single(businessId: String): Favorite
}
