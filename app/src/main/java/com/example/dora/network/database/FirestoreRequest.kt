package com.example.dora.network.database

import com.google.firebase.firestore.Filter

data class FirestoreRequest(
    val data: Any? = null,
    val collection: String,
    val document: String? = null,
    val updates: Map<String, Any>? = null,
    val where: Filter? = null,
    val offline: Boolean = false,
)
