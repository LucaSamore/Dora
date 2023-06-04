package com.example.dora

import com.example.dora.common.validation.UserValidator
import com.example.dora.common.validation.Validator
import org.junit.Assert.*
import org.junit.Test

class TestUserValidator {

  @Test
  fun `Test login credentials validation PASS`() {
    assertEquals(UserValidator.validateEmailAddress("test@gmail.com").status, Validator.Status.PASS)

    assertEquals(UserValidator.validatePassword("Test123!").status, Validator.Status.PASS)
  }

  @Test
  fun `Test login credentials validation REJECT`() {
    assertEquals(
      UserValidator.validateEmailAddress("invalid_email").status,
      Validator.Status.REJECT
    )

    assertEquals(UserValidator.validatePassword("invalid_password").status, Validator.Status.REJECT)
  }

  @Test
  fun `Test register credentials validation PASS`() {
    assertEquals(UserValidator.validateFirstOrLastName("Mario").status, Validator.Status.PASS)

    assertEquals(UserValidator.validateFirstOrLastName("Rossi").status, Validator.Status.PASS)
  }

  @Test
  fun `Test register credentials validation REJECT`() {
    assertEquals(UserValidator.validateFirstOrLastName("M").status, Validator.Status.REJECT)

    assertEquals(UserValidator.validateFirstOrLastName("R").status, Validator.Status.REJECT)
  }
}
