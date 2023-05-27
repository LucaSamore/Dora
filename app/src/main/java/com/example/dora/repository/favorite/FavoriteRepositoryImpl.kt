package com.example.dora.repository.favorite

import androidx.annotation.WorkerThread
import com.example.dora.database.dao.FavoriteDAO
import com.example.dora.database.entity.Favorite
import kotlinx.coroutines.flow.Flow

class FavoriteRepositoryImpl(private val favoriteDAO: FavoriteDAO) : FavoriteRepository {
    override fun getFavorites(): Flow<List<Favorite>> = favoriteDAO.getFavorites()

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
}
