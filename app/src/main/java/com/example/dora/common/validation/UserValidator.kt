package com.example.dora.common.validation

import org.apache.commons.validator.routines.EmailValidator

object UserValidator {
  private const val NAME_MIN_LENGTH = 2
  private const val NAME_MAX_LENGTH = 100
  private const val PASSWORD_MIN_LENGTH = 8
  private const val PASSWORD_MAX_LENGTH = 50

  internal fun validateFirstOrLastName(firstOrLastName: String): ValidationResult =
    Validator.validate(
      firstOrLastName,
      Validator.Rule(
        test = { firstOrLastName.isNotEmpty() },
        errorMessage = "First name or last name is required"
      ),
      Validator.Rule(
        test = { firstOrLastName.length in NAME_MIN_LENGTH..NAME_MAX_LENGTH },
        errorMessage = "First name or last name is too short"
      )
    )

  internal fun validateEmailAddress(emailAddress: String): ValidationResult =
    Validator.validate(
      emailAddress,
      Validator.Rule(
        test = { emailAddress.isNotEmpty() },
        errorMessage = "Email address is required"
      ),
      Validator.Rule(
        test = { EmailValidator.getInstance().isValid(emailAddress) },
        errorMessage = "Email address is not valid"
      )
    )

  internal fun validatePassword(password: String): ValidationResult =
    Validator.validate(
      password,
      Validator.Rule(test = { password.isNotEmpty() }, errorMessage = "Password is required"),
      Validator.Rule(
        test = { password.length in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH },
        errorMessage = "Password must be at least $PASSWORD_MIN_LENGTH characters"
      ),
      Validator.Rule(
        test = { password.firstOrNull { it.isDigit() } != null },
        errorMessage = "Password must contain at least a number"
      ),
      Validator.Rule(
        test = { password.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } != null },
        errorMessage = "Password must contain at least one uppercase letter"
      ),
      Validator.Rule(
        test = { password.filter { it.isLetter() }.firstOrNull { it.isLowerCase() } != null },
        errorMessage = "Password must contain at least one lowercase letter"
      ),
      Validator.Rule(
        test = { password.firstOrNull { !it.isLetterOrDigit() } != null },
        errorMessage = "Password must contain at least one letter and one number"
      )
    )
}
