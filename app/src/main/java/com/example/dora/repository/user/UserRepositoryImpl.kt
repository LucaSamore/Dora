package com.example.dora.repository.user

import com.example.dora.common.Location
import com.example.dora.network.database.FirestoreAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class UserRepositoryImpl(
    private val firestoreAPI: FirestoreAPI = FirestoreAPI(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    override suspend fun updateLocation(location: Location) {
        TODO("Not yet implemented")
    }
}