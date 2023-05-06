package com.example.dora.network.auth

import com.example.dora.common.auth.Credentials
import com.example.dora.common.validation.UserValidator
import com.example.dora.network.NetworkRequest
import com.example.dora.network.NetworkResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.tasks.Task
import com.example.dora.common.validation.ValidationResult
import com.example.dora.common.validation.ValidationStatus
import com.example.dora.network.api.AuthenticationAPI
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthAPI(private val auth: FirebaseAuth = Firebase.auth) : AuthenticationAPI<Credentials, Task<AuthResult>, Throwable> {

    override fun signUpWithEmailAndPassword(request: NetworkRequest<Credentials>) : NetworkResponse<Task<AuthResult>, Throwable> {
        val validationResult = validateCredentials(request.body)

        if (validationResult.data!! == ValidationStatus.REJECT) {
            return NetworkResponse(null, validationResult.error)
        }

        val authenticationResult =
            auth.createUserWithEmailAndPassword(request.body.emailAddress, request.body.password)

        return NetworkResponse(authenticationResult, null)
    }

    override fun signInWithEmailAndPassword(request: NetworkRequest<Credentials>): NetworkResponse<Task<AuthResult>, Throwable> {
        val validationResult = validateCredentials(request.body)

        if (validationResult.data!! == ValidationStatus.REJECT) {
            return NetworkResponse(null, validationResult.error)
        }

        val authenticationResult =
            auth.signInWithEmailAndPassword(request.body.emailAddress, request.body.password)

        return NetworkResponse(authenticationResult, null)
    }

    override fun isUserSignedIn(): Boolean = auth.currentUser != null

    override fun signOut() = auth.signOut()

    fun deleteUser() : Task<Void> = auth.currentUser?.delete()!!

    fun getFirebaseUser() : FirebaseUser? = auth.currentUser

    private fun validateCredentials(credentials: Credentials) : NetworkResponse<ValidationStatus, Throwable> {
        return when (credentials) {
            is Credentials.Login -> validateLoginCredentials(credentials)
            is Credentials.Register -> validateRegisterCredentials(credentials)
        }
    }

    private fun validateLoginCredentials(credentials: Credentials.Login) : NetworkResponse<ValidationStatus, Throwable> {
        // TODO: Make it better

        var response: NetworkResponse<ValidationStatus, Throwable>

        UserValidator.validateEmailAddress(credentials.emailAddress).also { response = onReject(it) }

        if (response.data == ValidationStatus.REJECT) {
            return response
        }

        UserValidator.validatePassword(credentials.password).also { response = onReject(it) }

        return response
    }

    private fun validateRegisterCredentials(credentials: Credentials.Register) : NetworkResponse<ValidationStatus, Throwable> {
        // TODO: Make it better

        var response: NetworkResponse<ValidationStatus, Throwable>

        UserValidator.validateFirstOrLastName(credentials.firstName).also { response = onReject(it) }

        if (response.data == ValidationStatus.REJECT) {
            return response
        }

        UserValidator.validateFirstOrLastName(credentials.lastName).also { response = onReject(it) }

        if (response.data == ValidationStatus.REJECT) {
            return response
        }

        UserValidator.validateEmailAddress(credentials.emailAddress).also { response = onReject(it) }

        if (response.data == ValidationStatus.REJECT) {
            return response
        }

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