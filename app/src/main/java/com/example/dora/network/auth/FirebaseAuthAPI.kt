package com.example.dora.network.auth

import com.example.dora.common.ValidationStatus
import com.example.dora.common.Validator
import com.example.dora.network.FirebaseRequest
import com.example.dora.network.FirebaseResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO:
// 1. Validate credentials (X)
// 2. Handle errors during validation and registration
// 2. Register user
// 3. Save user to firestore

class FirebaseAuthAPI(private var auth: FirebaseAuth = Firebase.auth) {

    internal fun signUpWithEmailAndPassword(request: FirebaseRequest<UserCredentials>) : FirebaseResponse<out Any, Throwable> {
        val validationResult = validateCredentials(request.body)

        if (validationResult.data!! == ValidationStatus.REJECT) {
            return validationResult
        }

        //val authResult = auth.createUserWithEmailAndPassword(emailAddress, password)
        return FirebaseResponse(null, null)
    }

    private fun validateCredentials(credentials: UserCredentials) : FirebaseResponse<ValidationStatus, Throwable> {
        val emailValidationResult = Validator.validateEmailAddress(credentials.emailAddress)
        val passwordValidationResult = Validator.validatePassword(credentials.password)

        if (emailValidationResult.status == ValidationStatus.REJECT) {
            return FirebaseResponse(ValidationStatus.REJECT, Throwable(emailValidationResult.message))
        }

        if (passwordValidationResult.status == ValidationStatus.REJECT) {
            return FirebaseResponse(ValidationStatus.REJECT, Throwable(passwordValidationResult.message))
        }

        return FirebaseResponse(ValidationStatus.PASS, null)
    }
}