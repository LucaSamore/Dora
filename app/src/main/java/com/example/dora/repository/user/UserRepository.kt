package com.example.dora.repository.user

import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.common.Location
import com.example.dora.model.User

interface UserRepository {
    suspend fun getUser(): Either<ErrorMessage, User?>
    suspend fun updateLocation(userId: String, location: Location): Either<ErrorMessage, Any?>
}
