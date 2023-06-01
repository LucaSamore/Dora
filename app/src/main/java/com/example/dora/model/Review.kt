package com.example.dora.model

import arrow.optics.optics
import com.example.dora.common.nowWithPattern

@optics
data class Review(
  val uuid: String? = null,
  val content: String? = null,
  val rating: Int? = null,
  val votes: Int? = null,
  val user: User? = null,
  val businessId: String? = null,
  val createdAt: String = nowWithPattern("yyyy-MM-dd HH:mm:ss")
) {
  companion object {
    const val collection = "reviews"
  }
}
