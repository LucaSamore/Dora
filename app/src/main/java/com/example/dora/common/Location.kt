package com.example.dora.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(val latitude: Double = 0.0, val longitude: Double = 0.0) : Parcelable {
  fun isNotSet() = latitude == 0.0 && longitude == 0.0
}

@Parcelize
data class BusinessPlace(
  val id: String? = null,
  val name: String? = null,
  val address: String? = null,
  val location: Location? = null
) : Parcelable
