package com.example.dora.network.auth

import com.example.dora.common.validation.UserValidator
import com.example.dora.network.NetworkRequest
import com.example.dora.network.NetworkResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.tasks.Task
import com.example.dora.common.validation.ValidationResult
import com.example.dora.common.validation.ValidationStatus

// TODO:
// 1. Validate credentials (X)
// 2. Handle errors during validation and registration
// 2. Register user (X)
// 3. Save user to firestore
// 4. Login (X)
// 5. Validate login credentials


class FirebaseAuthAPI(private var auth: FirebaseAuth = Firebase.auth) : AuthenticationAPI<Credentials, Task<*>, Throwable> {

    override fun signUpWithEmailAndPassword(request: NetworkRequest<Credentials>) : NetworkResponse<Task<*>, Throwable> {
        val validationResult = validateCredentials(request.body)

        if (validationResult.data!! == ValidationStatus.REJECT) {
            return NetworkResponse(null, validationResult.error)
        }

        // TODO: Store new account to firestore

        val authenticationResult = auth.createUserWithEmailAndPassword(request.body.emailAddress, request.body.password)

        return NetworkResponse(authenticationResult, null)
    }

    override fun signInWithEmailAndPassword(request: NetworkRequest<Credentials>): NetworkResponse<Task<*>, Throwable> {
        val validationResult = validateCredentials(request.body)

        if (validationResult.data!! == ValidationStatus.REJECT) {
            return NetworkResponse(null, validationResult.error)
        }

        val authenticationResult = auth.signInWithEmailAndPassword(request.body.emailAddress, request.body.password)

        return NetworkResponse(authenticationResult, null)
    }

    override fun signOut() = auth.signOut()

    override fun deleteUser(): NetworkResponse<Task<*>, Throwable> {
        return NetworkResponse(auth.currentUser?.delete(), null)
    }

    private fun validateCredentials(credentials: Credentials) : NetworkResponse<ValidationStatus, Throwable> {
        return when (credentials) {
            is Credentials.Login -> validateLoginCredentials(credentials)
            is Credentials.Register -> validateRegisterCredentials(credentials)
        }
    }

    private fun validateLoginCredentials(credentials: Credentials.Login) : NetworkResponse<ValidationStatus, Throwable> {
        var response: NetworkResponse<ValidationStatus, Throwable>

        UserValidator.validateEmailAddress(credentials.emailAddress).also { response = onReject(it) }
        UserValidator.validatePassword(credentials.password).also { response = onReject(it) }

        return response
    }

    private fun validateRegisterCredentials(credentials: Credentials.Register) : NetworkResponse<ValidationStatus, Throwable> {
        var response: NetworkResponse<ValidationStatus, Throwable>

        UserValidator.validateFirstOrLastName(credentials.firstName).also { response = onReject(it) }
        UserValidator.validateFirstOrLastName(credentials.lastName).also { response = onReject(it) }
        UserValidator.validateEmailAddress(credentials.emailAddress).also { response = onReject(it) }
        UserValidator.validatePassword(credentials.password).also { response = onReject(it) }

        return response
    }

    private fun onReject(result: ValidationResult) : NetworkResponse<ValidationStatus, Throwable> {
        return when (result.status) {
            ValidationStatus.PASS -> NetworkResponse(ValidationStatus.PASS, null)
            ValidationStatus.REJECT -> NetworkResponse(ValidationStatus.REJECT, Throwable(result.message))
        }
    }
}