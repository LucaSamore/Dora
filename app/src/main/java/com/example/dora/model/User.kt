package com.example.dora.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class User(
    val uid: String,
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val password: String,
    val location: String?,
    val profilePicture: String?,
    val createdAt: String = LocalDateTime
        .now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
)