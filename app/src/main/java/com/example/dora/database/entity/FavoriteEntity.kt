package com.example.dora.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey @ColumnInfo(name = "uuid") val uuid: String,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "businessId") val businessId: String,
)