package com.example.dora.common

enum class ValidationStatus { PASS, REJECT }

class ValidationResult(var status: ValidationStatus, var message: String?)

object Validator {
    private fun <T> validate(
        subject: T,
        vararg filters: (subject:  T) -> Boolean
    ) : ValidationResult {

        val validationResult = ValidationResult(ValidationStatus.PASS, null)

        for (filter in filters) {
            if (!filter(subject)) {
                validationResult.status = ValidationStatus.REJECT
                break
            }
        }

        return validationResult
    }

    internal fun validateEmailAddress(emailAddress: String): ValidationResult {
        return validate(
            emailAddress,
            { e -> e.isNotEmpty() },
            { e -> e.matches(Regexs.emailAddress) }
        ).also {
            if (it.status == ValidationStatus.REJECT) {
                it.message = "Email address is not valid"
            }
        }
    }

    internal fun validatePassword(password: String): ValidationResult {
        return validate(
            password,
            { p -> p.isNotEmpty() },
            { p -> p.matches(Regexs.password)}
        ).also {
            if (it.status == ValidationStatus.REJECT) {
                it.message = "Password is not valid"
            }
        }
    }
}