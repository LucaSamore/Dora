package com.example.dora.repository.auth

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.dora.common.ErrorMessage
import com.example.dora.common.auth.Credentials
import com.example.dora.model.User
import com.example.dora.network.NetworkRequest
import com.example.dora.network.auth.FirebaseAuthAPI
import com.example.dora.network.database.FirestoreAPI
import com.example.dora.network.database.FirestoreRequest
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl(
    private val firebaseAuthAPI: FirebaseAuthAPI = FirebaseAuthAPI(),
    private val firestoreAPI: FirestoreAPI = FirestoreAPI()
) : AuthenticationRepository {

    override suspend fun signInWithEmailAndPassword(credentials: Credentials.Login): Either<ErrorMessage, AuthResult> {
        return withContext(Dispatchers.IO) {
            firebaseAuthAPI
                .signInWithEmailAndPassword(NetworkRequest.of(credentials))
                .toEither()
                .let {
                    when (it) {
                        is Either.Left -> onValidationError(it.value).left()
                        is Either.Right -> onAuthenticationComplete(it.value!!)
                    }
                }
        }
    }

    override suspend fun signUpWithEmailAndPassword(credentials: Credentials.Register): Either<ErrorMessage, AuthResult> {
        return withContext(Dispatchers.IO) {
            firebaseAuthAPI
                .signUpWithEmailAndPassword(NetworkRequest.of(credentials))
                .toEither()
                .let {
                    when (it) {
                        is Either.Left -> onValidationError(it.value).left()
                        is Either.Right -> onAccountCreated(
                            onAuthenticationComplete(it.value!!),
                            credentials
                        )
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

    override suspend fun deleteUser(): Either<ErrorMessage, Void> {
        return withContext(Dispatchers.IO) {
            firebaseAuthAPI
                .deleteUser()
                .addOnCompleteListener {
                    when (it.isSuccessful) {
                        true -> it.result.right()
                        false -> ErrorMessage(it.exception?.message!!).left()
                    }
                }.await().right()
        }
    }

    private fun onValidationError(error: Throwable) : ErrorMessage = ErrorMessage(error.message!!)

    private suspend fun onAuthenticationComplete(authTask: Task<AuthResult>) : Either<ErrorMessage, AuthResult> {
        return authTask.addOnCompleteListener { task ->
            when (task.isSuccessful) {
                true -> task.result.right()
                false -> ErrorMessage(task.exception?.message!!).left()
            }
        }.await().right()
    }

    private suspend fun onAccountCreated(taskResult: Either<ErrorMessage, AuthResult>, credentials: Credentials.Register) : Either<ErrorMessage, AuthResult> {
        return when (taskResult) {
            is Either.Left -> taskResult.value.left()
            is Either.Right -> {
                when (val accountCreationResult = storeUserOnFirestore(credentials)) {
                    is Either.Left -> accountCreationResult.value.left()
                    is Either.Right -> taskResult.value.right()
                }
            }
        }
    }

    private suspend fun storeUserOnFirestore(credentials: Credentials.Register) : Either<ErrorMessage, Any?> {
        val user = User(
            uid = firebaseAuthAPI.getFirebaseUser()?.uid!!,
            firstName = credentials.firstName,
            lastName = credentials.lastName,
            emailAddress = credentials.emailAddress,
            password = credentials.password,
            location = credentials.location,
            profilePicture = credentials.profilePicture
        )

        val request = NetworkRequest.of(
            FirestoreRequest(
                data = user,
                collection = User.collection,
                document = user.uid,
            )
        )

        return firestoreAPI.insert(request).data?.addOnCompleteListener {
            when (it.isSuccessful) {
                true -> it.result.right()
                false -> ErrorMessage(it.exception?.message!!).left()
            }
        }?.await().right()
    }

}