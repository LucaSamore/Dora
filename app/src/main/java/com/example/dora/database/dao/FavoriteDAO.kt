package com.example.dora.database.dao

import androidx.room.*
import com.example.dora.database.entity.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDAO {
    @Query("SELECT * FROM favorites") fun getFavorites(): Flow<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insert(favorite: Favorite)

    @Delete suspend fun delete(favorite: Favorite)

    @Query("DELETE FROM favorites") suspend fun deleteAll()
}
