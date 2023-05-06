package com.example.dora.common.validation

import org.apache.commons.validator.routines.EmailValidator

object UserValidator : Validator {
    private const val PASSWORD_MIN_LENGTH = 8
    private const val PASSWORD_MAX_LENGTH = 50

    internal fun validateFirstOrLastName(firstOrLastName: String): ValidationResult {
        return validate(
            firstOrLastName,
            { f -> f.isNotEmpty() },
            { f -> f.length in 2..100 }
        ).also {
            bindErrorMessageIfRejected(it, "First name or last name is not valid")
        }
    }

    internal fun validateEmailAddress(emailAddress: String): ValidationResult {
        return validate(
            emailAddress,
            { e -> e.isNotEmpty() },
            { e -> EmailValidator.getInstance().isValid(e) }
        ).also {
            bindErrorMessageIfRejected(it, "Email address is not valid")
        }
    }

    internal fun validatePassword(password: String): ValidationResult {
        // because regular expressions are overrated
        return validate(
            password,
            { p -> p.isNotEmpty() },
            { p -> p.length in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH },
            { p -> p.firstOrNull { it.isDigit() } != null },
            { p -> p.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } != null },
            { p -> p.filter { it.isLetter() }.firstOrNull { it.isLowerCase() } != null },
            { p -> p.firstOrNull { !it.isLetterOrDigit() } != null }
        ).also {
            bindErrorMessageIfRejected(
                it,
                "Password must be at least $PASSWORD_MIN_LENGTH characters " +
                        "including an uppercase, a lowercase, a number and a special character"
            )
        }
    }
}