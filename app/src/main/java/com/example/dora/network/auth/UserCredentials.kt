package com.example.dora.network.auth

data class UserCredentials(
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val password: String,
    val location: String? = null,
    val profilePicture: String? = null,
)