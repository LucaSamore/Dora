package com.example.dora.model

import com.example.dora.common.nowWithPattern

data class Vote(
  val uuid: String? = null,
  val userId: String? = null,
  val reviewId: String? = null,
  val createdAt: String = nowWithPattern("yyyy-MM-dd HH:mm:ss")
) {
  companion object {
    const val collection = "votes"
  }
}
