package com.example.dora.repository.auth

import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.common.auth.Credentials
import com.google.firebase.auth.AuthResult

interface AuthenticationRepository {
    suspend fun signInWithEmailAndPassword(credentials: Credentials.Login) : Either<ErrorMessage, AuthResult>

    suspend fun signUpWithEmailAndPassword(credentials: Credentials.Register) : Either<ErrorMessage, AuthResult>

    suspend fun signOut()

    suspend fun isUserSignedIn(): Boolean

    suspend fun deleteUser() : Either<ErrorMessage, Void>
}