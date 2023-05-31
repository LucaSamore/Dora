package com.example.dora

import com.example.dora.common.validation.UserValidator
import com.example.dora.common.validation.ValidationStatus
import org.junit.Assert.*
import org.junit.Test

class TestUserValidator {

  @Test
  fun `Test login credentials validation PASS`() {
    assertEquals(UserValidator.validateEmailAddress("test@gmail.com").status, ValidationStatus.PASS)

    assertEquals(UserValidator.validatePassword("Test123!").status, ValidationStatus.PASS)
  }

  @Test
  fun `Test login credentials validation REJECT`() {
    assertEquals(
      UserValidator.validateEmailAddress("invalid_email").status,
      ValidationStatus.REJECT
    )

    assertEquals(UserValidator.validatePassword("invalid_password").status, ValidationStatus.REJECT)
  }

  @Test
  fun `Test register credentials validation PASS`() {
    assertEquals(UserValidator.validateFirstOrLastName("Mario").status, ValidationStatus.PASS)

    assertEquals(UserValidator.validateFirstOrLastName("Rossi").status, ValidationStatus.PASS)
  }

  @Test
  fun `Test register credentials validation REJECT`() {
    assertEquals(UserValidator.validateFirstOrLastName("M").status, ValidationStatus.REJECT)

    assertEquals(UserValidator.validateFirstOrLastName("R").status, ValidationStatus.REJECT)
  }
}
