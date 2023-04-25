package com.example.dora.network.auth

sealed class Credentials(val emailAddress: String, val password: String) {
    class Login(
        emailAddress: String,
        password: String
    ) : Credentials(emailAddress, password)

    class Register(
        emailAddress: String,
        password: String,
        val firstName: String,
        val lastName: String,
        val location: String? = null,
        val profilePicture: String? = null,
    ) : Credentials(emailAddress, password)
}
