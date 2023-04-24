package com.example.dora.network

data class FirebaseResponse<O, E : Throwable>(
    val data: O?,
    val error: E?
)