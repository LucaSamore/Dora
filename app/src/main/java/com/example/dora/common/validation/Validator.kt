package com.example.dora.common.validation

enum class ValidationStatus { PASS, REJECT }

class ValidationResult(
    var status: ValidationStatus,
    var message: String?
) {
    fun bindErrorMessageIfRejected(errorMessage: String) : ValidationResult {
        if (status == ValidationStatus.REJECT) {
            message = errorMessage
        }
        return this
    }
}

interface Validator {
    fun <T> validate(subject: T, vararg rules: (subject:  T) -> Boolean) : ValidationResult {
        val validationResult = ValidationResult(ValidationStatus.PASS, null)

        for (rule in rules) {
            if (!rule(subject)) {
                validationResult.status = ValidationStatus.REJECT
                break
            }
        }

        return validationResult
    }


}