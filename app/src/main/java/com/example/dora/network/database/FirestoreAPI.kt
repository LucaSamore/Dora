package com.example.dora.network.database

import com.example.dora.network.NetworkRequest
import com.example.dora.network.NetworkResponse
import com.example.dora.network.api.RemoteDatabaseAPI
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreAPI(private val db: FirebaseFirestore = Firebase.firestore) : RemoteDatabaseAPI<FirestoreRequest, Task<*>, Throwable> {
    override fun insert(
        request: NetworkRequest<FirestoreRequest>,
        autogenerateDocumentId: Boolean
    ): NetworkResponse<Task<*>, Throwable> {
        return when (autogenerateDocumentId) {
            true -> {
                val queryResult = db
                    .collection(request.body.collection)
                    .add(request.body.data)
                NetworkResponse(queryResult, null)
            }
            false -> {
                val queryResult = db
                    .collection(request.body.collection)
                    .document(request.body.document!!)
                    .set(request.body.data)
                NetworkResponse(queryResult, null)
            }
        }
    }

    override fun update(request: NetworkRequest<FirestoreRequest>): NetworkResponse<Task<*>, Throwable> {
        TODO("Not yet implemented")
    }

    override fun delete(request: NetworkRequest<FirestoreRequest>): NetworkResponse<Task<*>, Throwable> {
        val queryResult = db
            .collection(request.body.collection)
            .document(request.body.document!!)
            .delete()

        return NetworkResponse(queryResult, null)
    }

    override fun findOne(request: NetworkRequest<FirestoreRequest>): NetworkResponse<Task<*>, Throwable> {
        TODO("Not yet implemented")
    }

    override fun findMany(request: NetworkRequest<FirestoreRequest>): NetworkResponse<Task<*>, Throwable> {
        TODO("Not yet implemented")
    }

}