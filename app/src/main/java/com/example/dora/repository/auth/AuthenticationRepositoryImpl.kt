package com.example.dora.repository.auth

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import at.favre.lib.crypto.bcrypt.BCrypt
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
        return try {
            firebaseAuthAPI.deleteUser().await().right()
        } catch (e: Exception) {
            ErrorMessage(e.message!!).left()
        }
    }

    private fun onValidationError(error: Throwable) : ErrorMessage = ErrorMessage(error.message!!)

    private suspend fun onAuthenticationComplete(authTask: Task<AuthResult>) : Either<ErrorMessage, AuthResult> {
        return try {
            authTask.await().right()
        } catch (e: Exception) {
            ErrorMessage(e.message!!).left()
        }
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
        val user = createAccount(credentials)
        val request = createFirestoreRequest(user)

        return try {
            firestoreAPI.insert(request).data?.await().right()
        } catch (e: Exception) {
            ErrorMessage(e.message!!).left()
        }
    }

    private fun createAccount(credentials: Credentials.Register) : User = User(
        uid = firebaseAuthAPI.getFirebaseUser()?.uid!!,
        firstName = credentials.firstName,
        lastName = credentials.lastName,
        emailAddress = credentials.emailAddress,
        password = BCrypt.withDefaults().hashToString(12, credentials.password.toCharArray()),
        location = credentials.location,
        profilePicture = credentials.profilePicture
    )

    private fun createFirestoreRequest(user: User) : NetworkRequest<FirestoreRequest> =
        NetworkRequest.of(
            FirestoreRequest(
                data = user,
                collection = User.collection,
                document = user.uid,
            ),
        )
}