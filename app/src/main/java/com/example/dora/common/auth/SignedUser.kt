package com.example.dora.common.auth

data class SignedUser(
    val userId: String,
    val token: String,
    val firstName: String,
    val lastName: String,
)