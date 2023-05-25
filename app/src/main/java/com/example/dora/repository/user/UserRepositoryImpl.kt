package com.example.dora.repository.user

import android.net.Uri
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.dora.common.ErrorMessage
import com.example.dora.common.Location
import com.example.dora.common.SuccessMessage
import com.example.dora.datastore.UserDatastore
import com.example.dora.model.User
import com.example.dora.network.NetworkRequest
import com.example.dora.network.auth.FirebaseAuthAPI
import com.example.dora.network.database.FirestoreAPI
import com.example.dora.network.database.FirestoreRequest
import com.example.dora.network.storage.FirebaseStorageAPI
import com.example.dora.network.storage.FirebaseStorageRequest
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl
@Inject
constructor(
    private val firebaseAuthAPI: FirebaseAuthAPI,
    private val firestoreAPI: FirestoreAPI,
    private val firebaseStorageAPI: FirebaseStorageAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val userDatastore: UserDatastore
) : UserRepository {
    override suspend fun getUser(): Either<ErrorMessage, User> =
        withContext(ioDispatcher) {
            try {
                val request =
                    FirestoreRequest(
                        collection = User.collection,
                        document = userDatastore.userId.first(),
                    )

                firestoreAPI
                    .findOne(NetworkRequest.of(request))
                    .data
                    ?.findOneTask
                    ?.await()
                    ?.toObject(User::class.java)!!
                    .right()
            } catch (e: Exception) {
                ErrorMessage(e.message!!).left()
            }
        }

    override suspend fun updateUser(user: User): Either<ErrorMessage, SuccessMessage> =
        withContext(ioDispatcher) {
            try {
                val request =
                    FirestoreRequest(
                        collection = User.collection,
                        document = user.uid!!,
                        updates =
                            mapOf(
                                "firstName" to user.firstName!!,
                                "lastName" to user.lastName!!,
                                "emailAddress" to user.emailAddress!!,
                                "profilePicture" to user.profilePicture!!,
                            )
                    )

                updateAuthEmailAddress(user.emailAddress)

                return@withContext firestoreAPI
                    .update(NetworkRequest.of(request))
                    .data
                    ?.updateTask
                    ?.await()
                    .let { SuccessMessage("User updated successfully").right() }
            } catch (e: Exception) {
                ErrorMessage(e.message!!).left()
            }
        }

    override suspend fun updateAuthEmailAddress(
        newEmailAddress: String
    ): Either<ErrorMessage, SuccessMessage> =
        withContext(ioDispatcher) {
            try {
                firebaseAuthAPI.updateEmailAddress(newEmailAddress).await().let {
                    SuccessMessage("Email updated successfully").right()
                }
            } catch (e: Exception) {
                ErrorMessage(e.message!!).left()
            }
        }

    override suspend fun updateAuthPassword(
        newPlainPassword: String
    ): Either<ErrorMessage, SuccessMessage> =
        withContext(ioDispatcher) {
            try {
                firebaseAuthAPI.updatePassword(newPlainPassword).await().let {
                    SuccessMessage("Password updated successfully").right()
                }
            } catch (e: Exception) {
                ErrorMessage(e.message!!).left()
            }
        }

    override suspend fun updateLocation(location: Location): Either<ErrorMessage, SuccessMessage> =
        withContext(ioDispatcher) {
            try {
                firestoreAPI
                    .update(
                        NetworkRequest.of(
                            FirestoreRequest(
                                collection = User.collection,
                                document = userDatastore.userId.first(),
                                updates = mapOf("location" to location),
                            )
                        )
                    )
                    .data
                    ?.updateTask
                    ?.await()
                    .let { SuccessMessage("Location updated successfully").right() }
            } catch (e: Exception) {
                ErrorMessage(e.message!!).left()
            }
        }

    override suspend fun updateProfilePicture(
        userId: String,
        profilePictureUri: Uri
    ): Either<ErrorMessage, Uri> =
        withContext(ioDispatcher) {
            try {
                firebaseStorageAPI
                    .uploadFile(
                        NetworkRequest.of(
                            FirebaseStorageRequest(profilePictureUri, "$userId/profile")
                        )
                    )
                    .data
                    ?.await()!!
                    .right()
            } catch (e: Exception) {
                ErrorMessage(e.message!!).left()
            }
        }
}
