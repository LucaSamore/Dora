package com.example.dora.repository.auth

import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.common.auth.Credentials
import com.example.dora.common.auth.SignedUser

interface AuthenticationRepository {
    suspend fun signInWithEmailAndPassword(
        credentials: Credentials.Login
    ): Either<ErrorMessage, SignedUser>

    suspend fun signUpWithEmailAndPassword(
        credentials: Credentials.Register
    ): Either<ErrorMessage, SignedUser>

    suspend fun signOut()

    suspend fun isUserSignedIn(): Boolean

    suspend fun deleteUser(): Either<ErrorMessage, Void>
}
