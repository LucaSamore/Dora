package com.example.dora.network.database

data class FirestoreRequest<T: Any> (
    val data: T,
    val collection: String,
    val document: String?,
    val offline: Boolean = false
)