package com.example.dora.repository.auth

import arrow.core.Either
import com.example.dora.common.auth.Credentials
import com.example.dora.common.auth.SignedUser

@JvmInline
value class AuthFailed(val message: String)

interface AuthenticationRepository {
    suspend fun signInWithEmailAndPassword(credentials: Credentials.Login) : Either<AuthFailed, SignedUser>

    suspend fun signUpWithEmailAndPassword(credentials: Credentials.Register) : Either<AuthFailed, SignedUser>

    suspend fun signOut()

    suspend fun isUserSignedIn(): Boolean
}