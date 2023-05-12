package com.example.dora.repository.auth

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.dora.common.ErrorMessage
import com.example.dora.common.auth.Credentials
import com.example.dora.common.auth.SignedUser
import com.example.dora.model.User
import com.example.dora.network.NetworkRequest
import com.example.dora.network.auth.FirebaseAuthAPI
import com.example.dora.network.database.FirestoreAPI
import com.example.dora.network.database.FirestoreRequest
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseAuthRepository(
    private val firebaseAuthAPI: FirebaseAuthAPI = FirebaseAuthAPI(),
    private val firestoreAPI: FirestoreAPI = FirestoreAPI(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthenticationRepository {

    override suspend fun signInWithEmailAndPassword(
        credentials: Credentials.Login
    ): Either<ErrorMessage, SignedUser> =
        withContext(ioDispatcher) {
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

    override suspend fun signUpWithEmailAndPassword(
        credentials: Credentials.Register
    ): Either<ErrorMessage, SignedUser> =
        withContext(ioDispatcher) {
            firebaseAuthAPI
                .signUpWithEmailAndPassword(NetworkRequest.of(credentials))
                .toEither()
                .let {
                    when (it) {
                        is Either.Left -> onValidationError(it.value).left()
                        is Either.Right ->
                            onAccountCreated(onAuthenticationComplete(it.value!!), credentials)
                    }
                }
        }

    override suspend fun getCurrentUser(): SignedUser? {
        val user = firebaseAuthAPI.getFirebaseUser() ?: return null
        return SignedUser(user.uid)
    }

    override suspend fun signOut() {
        withContext(ioDispatcher) { firebaseAuthAPI.signOut() }
    }

    override suspend fun isUserSignedIn(): Boolean =
        withContext(ioDispatcher) { firebaseAuthAPI.isUserSignedIn() }

    override suspend fun deleteUser(): Either<ErrorMessage, Void> =
        withContext(ioDispatcher) {
            try {
                firebaseAuthAPI.deleteUser().await().right()
            } catch (e: Exception) {
                ErrorMessage(e.message!!).left()
            }
        }

    private fun onValidationError(error: Throwable): ErrorMessage = ErrorMessage(error.message!!)

    private suspend fun onAuthenticationComplete(
        authTask: Task<AuthResult>
    ): Either<ErrorMessage, SignedUser> =
        try {
            SignedUser.fromAuthResult(authTask.await()).right()
        } catch (e: Exception) {
            ErrorMessage(e.message!!).left()
        }

    private suspend fun onAccountCreated(
        taskResult: Either<ErrorMessage, SignedUser>,
        credentials: Credentials.Register
    ): Either<ErrorMessage, SignedUser> =
        when (taskResult) {
            is Either.Left -> taskResult.value.left()
            is Either.Right -> {
                when (val accountCreationResult = storeUserOnFirestore(credentials)) {
                    is Either.Left -> accountCreationResult.value.left()
                    is Either.Right -> taskResult.value.right()
                }
            }
        }

    private suspend fun storeUserOnFirestore(
        credentials: Credentials.Register
    ): Either<ErrorMessage, Any?> {
        val user = createAccount(credentials)
        val request = createFirestoreRequest(user)

        // TODO: Store profile picture to firebase storage, if present

        return try {
            firestoreAPI.insert(request).data?.await().right()
        } catch (e: Exception) {
            ErrorMessage(e.message!!).left()
        }
    }

    private fun createAccount(credentials: Credentials.Register): User {
        return User(
            uid = firebaseAuthAPI.getFirebaseUser()?.uid!!,
            firstName = credentials.firstName,
            lastName = credentials.lastName,
            emailAddress = credentials.emailAddress,
            password = BCrypt.withDefaults().hashToString(12, credentials.password.toCharArray()),
            profilePicture = credentials.profilePicture
        )
    }

    private fun createFirestoreRequest(user: User): NetworkRequest<FirestoreRequest> {
        val firestoreRequest =
            FirestoreRequest(
                data = user,
                collection = User.collection,
                document = user.uid,
            )
        return NetworkRequest.of(firestoreRequest)
    }
}
