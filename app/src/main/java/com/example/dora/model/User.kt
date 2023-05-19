package com.example.dora.model

import arrow.optics.optics
import com.example.dora.common.Location
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun nowWithPattern(pattern: String): String =
    LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern))

@optics
data class User(
    val uid: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val emailAddress: String? = null,
    val password: String? = null,
    val location: Location? = null,
    var profilePicture: String? = null,
    val createdAt: String = nowWithPattern("yyyy-MM-dd HH:mm:ss")
) {
    companion object {
        const val collection = "users"
    }
}

fun <T> opticsCompose(entity: T, vararg transformations: (T) -> T): T {
    return transformations.fold(entity) { e, transformation -> transformation(e) }
}
