package com.example.dora.network.database

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

data class FirestoreResponse(
    val collectionReference: CollectionReference? = null,
    val insertTask: Task<DocumentReference>? = null,
    val updateTask: Task<Void>? = null,
    val deleteTask: Task<Void>? = null,
    val singleTask: Task<DocumentSnapshot>? = null,
    val findTask: Task<QuerySnapshot>? = null,
)
