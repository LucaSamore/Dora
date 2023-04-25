package com.example.dora.network.auth

import com.example.dora.network.NetworkRequest
import com.example.dora.network.NetworkResponse

interface AuthenticationAPI<I,O,E : Throwable> {
    fun signUpWithEmailAndPassword(request: NetworkRequest<I>) : NetworkResponse<O,E>

    fun signOut()

    fun deleteUser() : NetworkResponse<O,E>
}