package com.example.dora.network.auth

import com.example.dora.common.ValidationResult
import com.example.dora.common.ValidationStatus
import com.example.dora.common.Validator
import com.example.dora.network.FirebaseRequest
import com.example.dora.network.FirebaseResponse
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO:
// 1. Validate credentials (X)
// 2. Handle errors during validation and registration
// 2. Register user (X)
// 3. Save user to firestore

class FirebaseAuthAPI(private var auth: FirebaseAuth = Firebase.auth) {

    internal fun signUpWithEmailAndPassword(request: FirebaseRequest<UserCredentials>) : FirebaseResponse<Task<AuthResult>, Throwable> {
        val validationResult = validateCredentials(request.body)

        if (validationResult.data!! == ValidationStatus.REJECT) {
            return FirebaseResponse(null, validationResult.error)
        }

        val authenticationResult = auth.createUserWithEmailAndPassword(request.body.emailAddress, request.body.password)

        return FirebaseResponse(authenticationResult, null)
    }

    internal fun signOut() = auth.signOut()

    internal fun deleteUser() = auth.currentUser!!.delete()


    private fun validateCredentials(credentials: UserCredentials) : FirebaseResponse<ValidationStatus, Throwable> {
        lateinit var response: FirebaseResponse<ValidationStatus, Throwable>

        Validator.validateFirstOrLastName(credentials.firstName).also { response = onReject(it) }
        Validator.validateFirstOrLastName(credentials.lastName).also { response = onReject(it) }
        Validator.validateEmailAddress(credentials.emailAddress).also { response = onReject(it) }
        Validator.validatePassword(credentials.password).also { response = onReject(it) }

        return response
    }

    private fun onReject(result: ValidationResult) : FirebaseResponse<ValidationStatus, Throwable> {
        return when (result.status) {
            ValidationStatus.PASS -> FirebaseResponse(ValidationStatus.PASS, null)
            ValidationStatus.REJECT -> FirebaseResponse(ValidationStatus.REJECT, Throwable(result.message))
        }
    }
}