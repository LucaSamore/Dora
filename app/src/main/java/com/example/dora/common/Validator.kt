package com.example.dora.common

object Validator {
    internal fun validateEmailAddress(emailAddress: String): Boolean =
        emailAddress.matches(Regex.emailAddress)

    internal fun validatePassword(password: String): Boolean =
        password.matches(Regex.password)
}