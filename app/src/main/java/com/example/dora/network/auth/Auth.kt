package com.example.dora.network.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Auth {
    private var auth: FirebaseAuth = Firebase.auth

    internal fun signUp(emailAddress: String, password: String): Boolean {
        // TODO: Validate credentials
        return auth
            .createUserWithEmailAndPassword(emailAddress, password)
            .isSuccessful
    }
}