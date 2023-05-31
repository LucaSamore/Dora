package com.example.dora.database.dao

import androidx.room.*
import com.example.dora.database.entity.Favorite

@Dao
interface FavoriteDAO {
  @Query("SELECT * FROM favorites") suspend fun getFavorites(): List<Favorite>

  @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insert(favorite: Favorite)

  @Delete suspend fun delete(favorite: Favorite)

  @Query("DELETE FROM favorites") suspend fun deleteAll()

  @Query("SELECT EXISTS (SELECT * FROM favorites WHERE businessId = :businessId)")
  suspend fun exists(businessId: String): Boolean

  @Query("SELECT * FROM favorites WHERE businessId = :businessId")
  suspend fun single(businessId: String): Favorite
}
