package com.example.dora.network.database

import com.example.dora.network.NetworkRequest
import com.example.dora.network.NetworkResponse
import com.example.dora.network.api.RemoteDatabaseAPI
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreAPI(private val db: FirebaseFirestore = Firebase.firestore) : RemoteDatabaseAPI<Task<*>, Throwable> {

    override fun <I> insert(toInsert: NetworkRequest<I>): NetworkResponse<Task<*>, Throwable> {
        TODO("Not yet implemented")
    }

    override fun <I> update(toUpdate: NetworkRequest<I>): NetworkResponse<Task<*>, Throwable> {
        TODO("Not yet implemented")
    }

    override fun <I> delete(toDelete: NetworkRequest<I>): NetworkResponse<Task<*>, Throwable> {
        TODO("Not yet implemented")
    }

    override fun <I> findOne(predicate: NetworkRequest<I>): NetworkResponse<Task<*>, Throwable> {
        TODO("Not yet implemented")
    }

    override fun <I> findMany(predicate: NetworkRequest<I>): NetworkResponse<Task<*>, Throwable> {
        TODO("Not yet implemented")
    }

}