package com.example.dora.common.validation

object Validator {
  enum class Status {
    PASS,
    REJECT
  }

  data class Result(val status: Status, val message: String?) {
    inline fun ifRejected(block: (Result) -> Unit) {
      if (status == Status.REJECT) {
        block(this)
      }
    }
  }
  data class Rule<T>(val test: (T) -> Boolean, val errorMessage: String)

  data class Pipe<T>(val subject: T, val ruleFunction: (T) -> Result)

  internal fun <T> validate(subject: T, vararg rules: Rule<T>): Result {
    rules.forEach { rule ->
      if (!rule.test(subject)) {
        return Result(status = Status.REJECT, message = rule.errorMessage)
      }
    }
    return Result(status = Status.PASS, message = null)
  }

  internal fun <T> pipeline(vararg pipes: Pipe<T>): Result {
    return pipes
      .map { p -> p.ruleFunction(p.subject) }
      .filter { r -> r.status == Status.REJECT }
      .getOrElse(0) { Result(status = Status.PASS, message = null) }
  }
}
