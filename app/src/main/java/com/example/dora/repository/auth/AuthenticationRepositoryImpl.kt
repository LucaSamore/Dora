package com.example.dora.repository.auth

import android.app.Activity
import android.content.Context
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.dora.common.auth.Credentials
import com.example.dora.common.auth.SignedUser
import com.example.dora.model.User
import com.example.dora.network.NetworkRequest
import com.example.dora.network.auth.FirebaseAuthAPI
import com.example.dora.network.database.FirestoreAPI
import com.example.dora.network.database.FirestoreRequest
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl(
    private val firebaseAuthAPI: FirebaseAuthAPI = FirebaseAuthAPI(),
    private val firestoreAPI: FirestoreAPI = FirestoreAPI()
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
                                is Either.Right -> {
                                    if (!storeUserOnFirestore(credentials)) {
                                        AuthFailed("Could not store user to firebase").left()
                                    }
                                    it.value.right()
                                }
                            }
                        }
                    }
                }
        }
    }

    override suspend fun signOut() {
        withContext(Dispatchers.IO) {
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
        authTask.addOnCompleteListener { task ->
            result = when (task.isSuccessful) {
                true -> SignedUser(uid = firebaseAuthAPI.getFirebaseUser()?.uid!!).right()
                false -> AuthFailed(task.exception?.message!!).left()
            }
        }
        Thread.sleep(1_000)
        return result
    }

    private fun storeUserOnFirestore(credentials: Credentials.Register) : Boolean {
        var result = false

        val user = User(
            uid = firebaseAuthAPI.getFirebaseUser()?.uid!!,
            firstName = credentials.firstName,
            lastName = credentials.lastName,
            emailAddress = credentials.emailAddress,
            password = credentials.password,
            location = credentials.location,
            profilePicture = credentials.profilePicture
        )

        firestoreAPI.insert(
            FirestoreRequest(
                data = user,
                collection = User.collection,
                document = user.uid,
            ).asNetworkRequest()
        ).data?.addOnCompleteListener { task ->
            result = task.isSuccessful
        }

        return result
    }
}