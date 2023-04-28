package com.example.dora.repository.auth

import arrow.core.Either
import com.example.dora.common.auth.Credentials
import com.example.dora.common.auth.SignedUser

@JvmInline value class AuthFailed(val message: String)

interface AuthenticationRepository {
    fun signInWithEmailAndPassword(credentials: Credentials.Login) : Either<AuthFailed, SignedUser>
    fun signUpWithEmailAndPassword(credentials: Credentials.Register) : Either<AuthFailed, SignedUser>
}