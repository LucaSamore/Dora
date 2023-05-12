package com.example.dora.repository.user

import com.example.dora.common.Location

interface UserRepository {
    suspend fun updateLocation(location: Location)
}