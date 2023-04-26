package com.example.dora

import com.example.dora.common.validation.UserValidator
import com.example.dora.common.validation.ValidationStatus
import org.junit.Test
import org.junit.Assert.*

class TestAuthValidation {
    private val emailPass = "test@gmail.com"
    private val emailReject = "invalid_email"
    private val passwordPass = "Test123!"
    private val passwordReject = "invalid_password"
    private val firstNamePass = "Mario"
    private val firstNameReject = "M"
    private val lastNamePass = "Rossi"
    private val lastNameReject = "R"

    @Test
    fun testLoginCredentialsPass() {
        val emailValidationResult = UserValidator.validateEmailAddress(emailPass)
        val passwordValidationResult = UserValidator.validatePassword(passwordPass)
        assertEquals(emailValidationResult.status, ValidationStatus.PASS)
        assertEquals(passwordValidationResult.status, ValidationStatus.PASS)
    }

    @Test
    fun testLoginCredentialsReject() {
        val emailValidationResult = UserValidator.validateEmailAddress(emailReject)
        val passwordValidationResult = UserValidator.validatePassword(passwordReject)
        assertEquals(emailValidationResult.status, ValidationStatus.REJECT)
        assertEquals(passwordValidationResult.status, ValidationStatus.REJECT)
    }

    @Test
    fun testRegisterCredentialsPass() {
        val firstNameValidationResult = UserValidator.validateFirstOrLastName(firstNamePass)
        val lastNameValidationResult = UserValidator.validateFirstOrLastName(lastNamePass)
        assertEquals(firstNameValidationResult.status, ValidationStatus.PASS)
        assertEquals(lastNameValidationResult.status, ValidationStatus.PASS)
    }

    @Test
    fun testRegisterCredentialsReject() {
        val firstNameValidationResult = UserValidator.validateFirstOrLastName(firstNameReject)
        val lastNameValidationResult = UserValidator.validateFirstOrLastName(lastNameReject)
        assertEquals(firstNameValidationResult.status, ValidationStatus.REJECT)
        assertEquals(lastNameValidationResult.status, ValidationStatus.REJECT)
    }
}