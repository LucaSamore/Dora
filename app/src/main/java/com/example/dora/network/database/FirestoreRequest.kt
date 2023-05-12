package com.example.dora.network.database

data class FirestoreRequest(
    val data: Any? = null,
    val collection: String,
    val document: String? = null,
    val updates: Map<String, Any>? = null,
    val offline: Boolean = false,
)
