package com.example.dora.model

import arrow.optics.optics

@optics
data class Review(
  val uuid: String? = null,
  val content: String? = null,
  val rating: Int? = null,
  val votes: Int? = null,
  val user: User? = null,
  val business: Business? = null,
) {
  companion object {
    const val collection = "reviews"
  }
}
