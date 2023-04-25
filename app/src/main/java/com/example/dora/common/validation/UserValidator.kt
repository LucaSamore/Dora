package com.example.dora.common.validation

object UserValidator : Validator {

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
            { e -> e.matches(Regexs.emailAddress) }
        ).also {
            bindErrorMessageIfRejected(it, "Email address is not valid")
        }
    }

    internal fun validatePassword(password: String): ValidationResult {
        return validate(
            password,
            { p -> p.isNotEmpty() },
            // { p -> p.matches(Regexs.password)}
        ).also {
            bindErrorMessageIfRejected(it, "Password is not valid")
        }
    }
}