package com.example.dora.common.auth

import com.google.firebase.auth.AuthResult

data class SignedUser(
    val uid: String,
) {
    companion object {
        fun fromAuthResult(authResult: AuthResult) : SignedUser =
            SignedUser(authResult.user?.uid!!)
    }
}