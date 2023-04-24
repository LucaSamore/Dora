package com.example.dora.model

import java.time.LocalDateTime

data class User(
    val uuid: String,
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val password: String,
    val location: String?,
    val profilePicture: String?,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)