package com.example.dora.model

import arrow.optics.optics
import com.example.dora.common.Location
import com.example.dora.common.nowWithPattern

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
