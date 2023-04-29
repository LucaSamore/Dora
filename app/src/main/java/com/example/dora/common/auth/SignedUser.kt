package com.example.dora.common.auth

data class SignedUser(
    val userId: String? = null,
    val token: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
)