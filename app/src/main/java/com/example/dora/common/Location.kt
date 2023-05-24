package com.example.dora.common

data class Location(val latitude: Double = 0.0, val longitude: Double = 0.0)

data class BusinessPlace(
    val id: String,
    val name: String,
    val address: String,
    val location: Location
)
