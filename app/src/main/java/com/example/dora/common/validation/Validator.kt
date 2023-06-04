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
  data class Rule<T>(val test: (T) -> Boolean, val errorMessage: String)

  data class Pipe<T>(val subject: T, val ruleFunction: (T) -> ValidationResult)

  internal fun <T> validate(subject: T, vararg rules: Rule<T>): ValidationResult {
    rules.forEach { rule ->
      if (!rule.test(subject)) {
        return ValidationResult(status = ValidationStatus.REJECT, message = rule.errorMessage)
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
