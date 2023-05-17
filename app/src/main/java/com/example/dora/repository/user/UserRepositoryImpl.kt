package com.example.dora.repository.user

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.dora.common.ErrorMessage
import com.example.dora.common.Location
import com.example.dora.datastore.UserDatastore
import com.example.dora.model.User
import com.example.dora.network.NetworkRequest
import com.example.dora.network.database.FirestoreAPI
import com.example.dora.network.database.FirestoreRequest
import com.example.dora.network.storage.FirebaseStorageAPI
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl
@Inject
constructor(
    private val firestoreAPI: FirestoreAPI,
    private val firebaseStorageAPI: FirebaseStorageAPI,
    private val ioDispatcher: CoroutineDispatcher,
    private val userDatastore: UserDatastore
) : UserRepository {
    override suspend fun getUser(): Either<ErrorMessage, User?> =
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
                    ?.toObject(User::class.java)
                    .right()
            } catch (e: Exception) {
                ErrorMessage(e.message!!).left()
            }
        }

    override suspend fun updateLocation(
        userId: String,
        location: Location
    ): Either<ErrorMessage, Any?> =
        withContext(ioDispatcher) {
            try {
                firestoreAPI
                    .update(
                        NetworkRequest.of(
                            FirestoreRequest(
                                collection = User.collection,
                                document = userId,
                                updates = mapOf("location" to location),
                            )
                        )
                    )
                    .data
                    ?.updateTask
                    ?.await()
                    .right()
            } catch (e: Exception) {
                ErrorMessage(e.message!!).left()
            }
        }
}
