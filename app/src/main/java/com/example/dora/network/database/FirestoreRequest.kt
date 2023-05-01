package com.example.dora.network.database

import com.example.dora.network.NetworkRequest

data class FirestoreRequest(
    val data: Any,
    val collection: String,
    val document: String? = null,
    val offline: Boolean = false
) {
    fun asNetworkRequest() : NetworkRequest<FirestoreRequest> = NetworkRequest(this)
}