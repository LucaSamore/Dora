package com.example.dora.common

fun <T> opticsCompose(entity: T, vararg transformations: (T) -> T): T {
    return transformations.fold(entity) { e, transformation -> transformation(e) }
}