package com.example.dora.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(val latitude: Double = 0.0, val longitude: Double = 0.0) : Parcelable

@Parcelize
data class BusinessPlace(
    val id: String,
    val name: String,
    val address: String,
    val location: Location
) : Parcelable
