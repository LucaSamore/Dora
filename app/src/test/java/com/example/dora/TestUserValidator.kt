package com.example.dora

import com.example.dora.common.validation.Status
import com.example.dora.common.validation.UserValidator
import org.junit.Assert.*
import org.junit.Test

class TestUserValidator {

  @Test
  fun `Test login credentials validation PASS`() {
    assertEquals(UserValidator.validateEmailAddress("test@gmail.com").status, Status.PASS)

    assertEquals(UserValidator.validatePassword("Test123!").status, Status.PASS)
  }

  @Test
  fun `Test login credentials validation REJECT`() {
    assertEquals(UserValidator.validateEmailAddress("invalid_email").status, Status.REJECT)

    assertEquals(UserValidator.validatePassword("invalid_password").status, Status.REJECT)
  }

  @Test
  fun `Test register credentials validation PASS`() {
    assertEquals(UserValidator.validateFirstOrLastName("Mario").status, Status.PASS)

    assertEquals(UserValidator.validateFirstOrLastName("Rossi").status, Status.PASS)
  }

  @Test
  fun `Test register credentials validation REJECT`() {
    assertEquals(UserValidator.validateFirstOrLastName("M").status, Status.REJECT)

    assertEquals(UserValidator.validateFirstOrLastName("R").status, Status.REJECT)
  }
}
