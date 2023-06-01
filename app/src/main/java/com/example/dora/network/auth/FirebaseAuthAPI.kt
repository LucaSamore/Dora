package com.example.dora.network.auth

import com.example.dora.common.auth.Credentials
import com.example.dora.common.validation.UserValidator
import com.example.dora.common.validation.ValidationStatus
import com.example.dora.common.validation.Validator
import com.example.dora.network.NetworkRequest
import com.example.dora.network.NetworkResponse
import com.example.dora.network.api.AuthenticationAPI
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthAPI(private val auth: FirebaseAuth = Firebase.auth) :
  AuthenticationAPI<Credentials, Task<AuthResult>, Throwable> {

  override fun signUpWithEmailAndPassword(
    request: NetworkRequest<Credentials>
  ): NetworkResponse<Task<AuthResult>, Throwable> {
    val validationResult = validateCredentials(request.body)

    if (validationResult.data!! == ValidationStatus.REJECT) {
      return NetworkResponse(null, validationResult.error)
    }

    val authenticationResult =
      auth.createUserWithEmailAndPassword(request.body.emailAddress, request.body.password)

    return NetworkResponse(authenticationResult, null)
  }

  override fun signInWithEmailAndPassword(
    request: NetworkRequest<Credentials>
  ): NetworkResponse<Task<AuthResult>, Throwable> {
    val validationResult = validateCredentials(request.body)

    if (validationResult.data!! == ValidationStatus.REJECT) {
      return NetworkResponse(null, validationResult.error)
    }

    val authenticationResult =
      auth.signInWithEmailAndPassword(request.body.emailAddress, request.body.password)

    return NetworkResponse(authenticationResult, null)
  }

  fun updateEmailAddress(newEmailAddress: String): Task<Void> =
    auth.currentUser!!.updateEmail(newEmailAddress)

  fun updatePassword(newPassword: String): Task<Void> =
    auth.currentUser!!.updatePassword(newPassword)

  override fun isUserSignedIn(): Boolean = auth.currentUser != null

  override fun signOut() = auth.signOut()

  fun deleteUser(): Task<Void> = auth.currentUser?.delete()!!

  fun getFirebaseUser(): FirebaseUser? = auth.currentUser

  private fun validateCredentials(
    credentials: Credentials
  ): NetworkResponse<ValidationStatus, Throwable> {
    return when (credentials) {
      is Credentials.Login -> validateLoginCredentials(credentials)
      is Credentials.Register -> validateRegisterCredentials(credentials)
    }
  }

  private fun validateLoginCredentials(
    credentials: Credentials.Login
  ): NetworkResponse<ValidationStatus, Throwable> {

    Validator.pipeline(
        Pair(credentials.emailAddress, UserValidator::validateEmailAddress),
        Pair(credentials.password, UserValidator::validatePassword)
      )
      .ifRejected {
        return NetworkResponse(ValidationStatus.REJECT, Throwable(it.message))
      }

    return NetworkResponse(ValidationStatus.PASS, null)
  }

  private fun validateRegisterCredentials(
    credentials: Credentials.Register
  ): NetworkResponse<ValidationStatus, Throwable> {

    Validator.pipeline(
        Pair(credentials.firstName, UserValidator::validateFirstOrLastName),
        Pair(credentials.lastName, UserValidator::validateFirstOrLastName),
        Pair(credentials.emailAddress, UserValidator::validateEmailAddress),
        Pair(credentials.password, UserValidator::validatePassword),
      )
      .ifRejected {
        return NetworkResponse(ValidationStatus.REJECT, Throwable(it.message))
      }

    return NetworkResponse(ValidationStatus.PASS, null)
  }
}
