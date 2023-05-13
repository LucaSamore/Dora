package com.example.dora.network.storage

import com.example.dora.network.NetworkRequest
import com.example.dora.network.NetworkResponse
import com.example.dora.network.api.StorageAPI
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File

class FirebaseStorageAPI(storage: FirebaseStorage = Firebase.storage) :
    StorageAPI<FirebaseStorageRequest, Task<*>, Throwable> {

    private val storageReference = storage.reference

    override fun uploadFile(
        file: NetworkRequest<FirebaseStorageRequest>
    ): NetworkResponse<Task<*>, Throwable> {
        val reference = storageReference.child(file.body.fullReference())
        val uploadTask = reference.putFile(file.body.fileUri)
        return NetworkResponse(uploadTask, null)
    }

    override fun downloadFile(
        request: NetworkRequest<FirebaseStorageRequest>
    ): NetworkResponse<Task<*>, Throwable> {
        val reference = storageReference.child(request.body.reference)
        val file = File.createTempFile(request.body.downloadFileName!!, "jpg")
        return NetworkResponse(reference.getFile(file), null)
    }
}
