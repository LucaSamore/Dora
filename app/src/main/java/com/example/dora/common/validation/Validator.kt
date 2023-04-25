package com.example.dora.common.validation

import android.util.Log

private const val TAG = "Validator"

interface Validator {
    fun <T> validate(subject: T, vararg rules: (subject:  T) -> Boolean) : ValidationResult {
        val validationResult = ValidationResult(ValidationStatus.PASS, null)

        for (rule in rules) {
            if (!rule(subject)) {
                validationResult.status = ValidationStatus.REJECT
                Log.i(TAG, "Rejected")
                break
            }
        }

        return validationResult
    }

    fun bindErrorMessageIfRejected(validationResult: ValidationResult, message: String): ValidationResult {
        if (validationResult.status == ValidationStatus.REJECT) {
            validationResult.message = message
        }
        return validationResult
    }
}