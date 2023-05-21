package com.example.dora.repository.auth

import android.net.Uri
import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.dora.common.ErrorMessage
import com.example.dora.common.SuccessMessage
import com.example.dora.common.auth.Credentials
import com.example.dora.common.auth.SignedUser
import com.example.dora.datastore.UserDatastore
import com.example.dora.model.User
import com.example.dora.network.NetworkRequest
import com.example.dora.network.auth.FirebaseAuthAPI
import com.example.dora.network.database.FirestoreAPI
import com.example.dora.network.database.FirestoreRequest
import com.example.dora.network.storage.FirebaseStorageAPI
import com.example.dora.network.storage.FirebaseStorageRequest
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseAuthRepository
@Inject
constructor(
    private val firebaseAuthAPI: FirebaseAuthAPI,
    private val firestoreAPI: FirestoreAPI,
    private val firebaseStorageAPI: FirebaseStorageAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val userDatastore: UserDatastore,
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

    override suspend fun deleteUser(): Either<ErrorMessage, SuccessMessage> =
        withContext(ioDispatcher) {
            try {
                firebaseAuthAPI.deleteUser().await().let {
                    SuccessMessage("Account deleted successfully").right()
                }
            } catch (e: Exception) {
                ErrorMessage(e.message!!).left()
            }
        }

    private fun onValidationError(error: Throwable): ErrorMessage = ErrorMessage(error.message!!)

    private suspend fun onAuthenticationComplete(
        authTask: Task<AuthResult>
    ): Either<ErrorMessage, SignedUser> =
        try {
            val user = SignedUser.fromAuthResult(authTask.await())
            userDatastore.saveUserIdToDataStore(user.uid)
            user.right()
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
                when (
                    val accountCreationResult =
                        storeUserOnFirestore(credentials, taskResult.value.uid)
                ) {
                    is Either.Left -> accountCreationResult.value.left()
                    is Either.Right -> taskResult.value.right()
                }
            }
        }

    private suspend fun storeUserOnFirestore(
        credentials: Credentials.Register,
        signedUserId: String,
    ): Either<ErrorMessage, Any?> {
        val user = createAccount(credentials)

        if (user.profilePicture != null) {
            val storeResult =
                storeUserProfilePictureToFirebaseStorage(
                    Uri.parse(user.profilePicture),
                    signedUserId
                )
            if (storeResult.isLeft()) {
                return storeResult.map { it.left() }
            }
            user.profilePicture = storeResult.getOrElse { null }.toString()
        }

        val request = createFirestoreRequest(user)

        return try {
            firestoreAPI.insert(request).data?.insertTask?.await().right()
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
            profilePicture = credentials.profilePicture.toString()
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

    private suspend fun storeUserProfilePictureToFirebaseStorage(
        file: Uri,
        signedUserId: String
    ): Either<ErrorMessage, Uri> {
        return try {
            firebaseStorageAPI
                .uploadFile(
                    NetworkRequest.of(FirebaseStorageRequest(file, "$signedUserId/profile"))
                )
                .data
                ?.await()!!
                .right()
        } catch (e: Exception) {
            ErrorMessage(e.message!!).left()
        }
    }
}
