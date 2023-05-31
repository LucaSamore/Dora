package com.example.dora.common.validation

enum class ValidationStatus {
  PASS,
  REJECT
}

class ValidationResult(var status: ValidationStatus, var message: String?) {
  inline fun catch(block: (ValidationResult) -> Unit) {
    if (status == ValidationStatus.REJECT) {
      block(this)
    }
  }
}

object Validator {
  fun <T> validate(
    subject: T,
    vararg rules: (subject: T) -> Pair<Boolean, String>
  ): ValidationResult {
    val validationResult = ValidationResult(status = ValidationStatus.PASS, message = null)
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

  fun <T> pipeline(vararg functions: Pair<T, (T) -> ValidationResult>): ValidationResult {
    for (function in functions) {
      val result = function.second(function.first)
      if (result.status == ValidationStatus.REJECT) {
        return result
      }
    }
    return ValidationResult(status = ValidationStatus.PASS, message = null)
  }
}
