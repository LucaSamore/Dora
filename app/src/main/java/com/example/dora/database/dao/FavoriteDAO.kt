package com.example.dora.database.dao

import androidx.room.*
import com.example.dora.database.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDAO {
    @Query("SELECT * FROM favorites")
    fun getFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: FavoriteEntity)

    @Delete
    suspend fun delete(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites")
    suspend fun deleteAll()
}