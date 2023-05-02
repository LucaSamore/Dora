package com.example.dora.repository.auth

import arrow.core.Either
import com.example.dora.common.auth.Credentials
import com.google.firebase.auth.AuthResult

@JvmInline
value class AuthFailed(val message: String)

interface AuthenticationRepository {
    suspend fun signInWithEmailAndPassword(credentials: Credentials.Login) : Either<AuthFailed, AuthResult>

    suspend fun signUpWithEmailAndPassword(credentials: Credentials.Register) : Either<AuthFailed, AuthResult>

    suspend fun signOut()

    suspend fun isUserSignedIn(): Boolean
}