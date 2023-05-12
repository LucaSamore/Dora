package com.example.dora.repository.user

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.dora.common.ErrorMessage
import com.example.dora.common.Location
import com.example.dora.model.User
import com.example.dora.network.NetworkRequest
import com.example.dora.network.database.FirestoreAPI
import com.example.dora.network.database.FirestoreRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val firestoreAPI: FirestoreAPI = FirestoreAPI(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    override suspend fun updateLocation(userId: String, location: Location) : Either<ErrorMessage, Any?> =
        withContext(ioDispatcher) {
            try {
                firestoreAPI.update(NetworkRequest.of(FirestoreRequest(
                    collection = User.collection,
                    document = userId,
                    updates = mapOf("location" to location),
                ))).data?.await().right()
            } catch (e: Exception) {
                ErrorMessage(e.message!!).left()
            }
        }
}