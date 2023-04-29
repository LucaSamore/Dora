package com.example.dora.repository.auth

import android.app.Activity
import android.content.Context
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.dora.common.auth.Credentials
import com.example.dora.common.auth.SignedUser
import com.example.dora.network.NetworkRequest
import com.example.dora.network.auth.FirebaseAuthAPI
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl(
    private val context: Context,
    private val firebaseAuthAPI: FirebaseAuthAPI = FirebaseAuthAPI()
) : AuthenticationRepository {

    override suspend fun signInWithEmailAndPassword(credentials: Credentials.Login): Either<AuthFailed, SignedUser> {
        return withContext(Dispatchers.IO) {
            firebaseAuthAPI
                .signInWithEmailAndPassword(NetworkRequest(credentials))
                .asEither()
                .let {
                    when (it) {
                        is Either.Left -> AuthFailed(it.value.message!!).left()
                        is Either.Right -> onAuthenticationComplete(it.value!!)
                    }
                }
        }
    }

    override suspend fun signUpWithEmailAndPassword(credentials: Credentials.Register): Either<AuthFailed, SignedUser> {
        return withContext(Dispatchers.IO) {
            firebaseAuthAPI
                .signUpWithEmailAndPassword(NetworkRequest(credentials))
                .asEither()
                .let { signUpResult ->
                    when (signUpResult) {
                        is Either.Left -> AuthFailed(signUpResult.value.message!!).left()
                        is Either.Right -> onAuthenticationComplete(signUpResult.value!!).let {
                            when (it) {
                                is Either.Left -> AuthFailed(it.value.message).left()
                                is Either.Right -> it.value.right()
                            }
                        }
                    }
                }
        }
    }

    override suspend fun signOut() {
        return withContext(Dispatchers.IO) {
            firebaseAuthAPI.signOut()
        }
    }

    override suspend fun isUserSignedIn(): Boolean {
        return withContext(Dispatchers.IO) {
            firebaseAuthAPI.isUserSignedIn()
        }
    }

    private fun onAuthenticationComplete(authTask: Task<*>) : Either<AuthFailed, SignedUser> {
        lateinit var result: Either<AuthFailed, SignedUser>
        authTask.addOnCompleteListener(context as Activity) { task ->
            result = when (task.isSuccessful) {
                true -> SignedUser().right() // TODO
                false -> AuthFailed(task.exception?.message!!).left()
            }
        }
        return  result
    }
}