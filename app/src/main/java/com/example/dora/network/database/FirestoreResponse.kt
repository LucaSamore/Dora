package com.example.dora.network.database

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

data class FirestoreResponse(
    val insertTask: Task<DocumentReference>? = null,
    val updateTask: Task<Void>? = null,
    val deleteTask: Task<Void>? = null,
    val findOneTask: Task<DocumentSnapshot>? = null,
)
