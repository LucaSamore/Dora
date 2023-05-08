package com.example.dora.common.validation

import org.apache.commons.validator.routines.EmailValidator

object UserValidator : Validator {
    private const val NAME_MIN_LENGTH = 2
    private const val NAME_MAX_LENGTH = 100
    private const val PASSWORD_MIN_LENGTH = 8
    private const val PASSWORD_MAX_LENGTH = 50

    internal fun validateFirstOrLastName(firstOrLastName: String): ValidationResult =
        validate(
            firstOrLastName,
            { f -> Pair(f.isNotEmpty(), "First name or last name is required") },
            { f ->
                Pair(
                    f.length in NAME_MIN_LENGTH..NAME_MAX_LENGTH,
                    "First name or last name is too short"
                )
            }
        )

    internal fun validateEmailAddress(emailAddress: String): ValidationResult =
        validate(
            emailAddress,
            { e -> Pair(e.isNotEmpty(), "Email address is required") },
            { e -> Pair(EmailValidator.getInstance().isValid(e), "Email address is not valid") }
        )

    internal fun validatePassword(password: String): ValidationResult =
        validate(
            password,
            { p -> Pair(p.isNotEmpty(), "Password is required") },
            { p ->
                Pair(
                    p.length in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH,
                    "Password must be at least $PASSWORD_MIN_LENGTH characters"
                )
            },
            { p ->
                Pair(
                    p.firstOrNull { it.isDigit() } != null,
                    "Password must contain at least a number"
                )
            },
            { p ->
                Pair(
                    p.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } != null,
                    "Password must contain at least one uppercase letter"
                )
            },
            { p ->
                Pair(
                    p.filter { it.isLetter() }.firstOrNull { it.isLowerCase() } != null,
                    "Password must contain at least one lowercase letter"
                )
            },
            { p ->
                Pair(
                    p.firstOrNull { !it.isLetterOrDigit() } != null,
                    "Password must contain at least one letter and one number"
                )
            }
        )
}
