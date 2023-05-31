package com.example.dora.network.api

import com.example.dora.network.NetworkRequest
import com.example.dora.network.NetworkResponse

interface AuthenticationAPI<I, O, E : Throwable> {
  fun signUpWithEmailAndPassword(request: NetworkRequest<I>): NetworkResponse<O, E>

  fun signInWithEmailAndPassword(request: NetworkRequest<I>): NetworkResponse<O, E>

  fun isUserSignedIn(): Boolean

  fun signOut()
}
