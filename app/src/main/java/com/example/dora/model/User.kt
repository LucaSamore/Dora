package com.example.dora.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun nowWithPattern(pattern: String): String =
    LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern))

data class User(
    val uid: String,
    val firstName: String,
    val lastName: String,
    val emailAddress: String,
    val password: String,
    val location: String?,
    val profilePicture: String?,
    val createdAt: String = nowWithPattern("yyyy-MM-dd HH:mm:ss")
) {
    companion object {
        const val collection = "users"
    }
}
