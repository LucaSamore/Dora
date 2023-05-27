package com.example.dora.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dora.database.dao.FavoriteDAO
import com.example.dora.database.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class DoraDatabase : RoomDatabase() {

    abstract fun favoriteDAO(): FavoriteDAO

    companion object {
        @Volatile
        private var INSTANCE: DoraDatabase ?= null

        fun getDatabase(context: Context): DoraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DoraDatabase::class.java,
                    "dora_database",
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}