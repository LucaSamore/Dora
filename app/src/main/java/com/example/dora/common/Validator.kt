package com.example.dora.common

enum class ValidationStatus {
    PASS,
    REJECT
}
data class ValidationResult(
    val status: ValidationStatus,
    val message: String?
)

object Validator {
    internal fun validateEmailAddress(emailAddress: String): ValidationResult =
        validateString(emailAddress, Regexs.emailAddress, "Email not valid")

    internal fun validatePassword(password: String): ValidationResult =
        validateString(password, Regexs.password, "Password not valid")

    private fun validateString(
        toValidate: String,
        regex: Regex,
        errorMessage: String
    ): ValidationResult {
        return when (toValidate.matches(regex)) {
            true -> ValidationResult(ValidationStatus.PASS, null)
            false -> ValidationResult(ValidationStatus.REJECT, errorMessage)
        }
    }
}