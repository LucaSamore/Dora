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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl(
    private val context: Context,
    private val firebaseAuthAPI: FirebaseAuthAPI = FirebaseAuthAPI()
) : AuthenticationRepository {

    override suspend fun signInWithEmailAndPassword(credentials: Credentials.Login): Either<AuthFailed, SignedUser> {
        return withContext(Dispatchers.IO) {
            lateinit var result: Either<AuthFailed, SignedUser>
            firebaseAuthAPI
                .signInWithEmailAndPassword(NetworkRequest(credentials))
                .asEither()
                .let {
                    when (it) {
                        is Either.Left -> result = AuthFailed(it.value.message!!).left()
                        is Either.Right -> it.value?.addOnCompleteListener(context as Activity) { task ->
                            result = when (task.isSuccessful) {
                                true -> SignedUser().right() // TODO: happy path
                                false -> AuthFailed(task.exception?.message!!).left()
                            }
                        }
                    }
                }
            return@withContext result
        }
    }

    override suspend fun signUpWithEmailAndPassword(credentials: Credentials.Register): Either<AuthFailed, SignedUser> {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }

    override suspend fun isUserSignedIn(): Boolean {
        TODO("Not yet implemented")
    }
}