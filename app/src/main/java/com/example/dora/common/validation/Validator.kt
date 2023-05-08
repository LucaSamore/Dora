package com.example.dora.common.validation

enum class ValidationStatus {
    PASS,
    REJECT
}

class ValidationResult(var status: ValidationStatus, var message: String?)

interface Validator {
    fun <T> validate(
        subject: T,
        vararg rules: (subject: T) -> Pair<Boolean, String>
    ): ValidationResult {
        val validationResult = ValidationResult(ValidationStatus.PASS, null)
        for (applyRule in rules) {
            val result = applyRule(subject)
            if (!result.first) {
                validationResult.status = ValidationStatus.REJECT
                validationResult.message = result.second
                break
            }
        }
        return validationResult
    }
}
