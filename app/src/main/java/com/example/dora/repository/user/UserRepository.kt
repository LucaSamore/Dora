package com.example.dora.repository.user

import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.common.Location

interface UserRepository {
    suspend fun updateLocation(userId: String, location: Location): Either<ErrorMessage, Any?>
}
