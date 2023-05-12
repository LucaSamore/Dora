package com.example.dora.model

import com.example.dora.common.Location
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
    val location: Location? = null,
    val profilePicture: String? = null,
    val createdAt: String = nowWithPattern("yyyy-MM-dd HH:mm:ss")
) {
    companion object {
        const val collection = "users"
    }
}
