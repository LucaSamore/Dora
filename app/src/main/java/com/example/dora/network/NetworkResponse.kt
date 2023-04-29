package com.example.dora.network

import arrow.core.Either
import arrow.core.left
import arrow.core.right

data class NetworkResponse<O, E : Throwable> (val data: O?, val error: E?) {
    fun asEither() : Either<E, O?> = when (error) {
        null -> data.right()
        else -> error.left()
    }
}