package com.example.dora.common.validation

enum class ValidationStatus {
  PASS,
  REJECT
}

class ValidationResult(var status: ValidationStatus, var message: String?) {
  inline fun ifRejected(block: (ValidationResult) -> Unit) {
    if (status == ValidationStatus.REJECT) {
      block(this)
    }
  }
}

object Validator {
  internal fun <T> validate(
    subject: T,
    vararg rules: (subject: T) -> Pair<Boolean, String>
  ): ValidationResult {
    rules
      .map { r -> r(subject) }
      .forEach { p ->
        if (!p.first) {
          return ValidationResult(status = ValidationStatus.REJECT, message = p.second)
        }
      }
    return ValidationResult(status = ValidationStatus.PASS, message = null)
  }

  internal fun <T> pipeline(vararg functions: Pair<T, (T) -> ValidationResult>): ValidationResult {
    return functions
      .map { f -> f.second(f.first) }
      .filter { r -> r.status == ValidationStatus.REJECT }
      .getOrElse(0) { ValidationResult(status = ValidationStatus.PASS, message = null) }
  }
}
