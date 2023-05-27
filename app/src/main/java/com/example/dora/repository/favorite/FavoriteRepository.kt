package com.example.dora.repository.favorite

import com.example.dora.database.entity.Favorite

interface FavoriteRepository {
    suspend fun insert(favorite: Favorite)

    suspend fun delete(favorite: Favorite)

    suspend fun deleteAll()
}
