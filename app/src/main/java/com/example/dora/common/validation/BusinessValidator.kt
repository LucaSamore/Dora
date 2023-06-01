package com.example.dora.common.validation

import android.net.Uri
import com.example.dora.common.BusinessPlace
import org.apache.commons.validator.routines.UrlValidator

object BusinessValidator {
  private const val NAME_MIN_LENGTH = 2
  private const val NAME_MAX_LENGTH = 50
  private const val PHONE_NUMBER_LENGTH = 10
  private const val MAX_IMAGES_NUMBER = 5
  private const val MIN_IMAGES_NUMBER = 1

  internal fun validateName(name: String): ValidationResult =
    Validator.validate(
      name,
      { n -> Pair(n.isNotEmpty(), "Business name is required") },
      { n -> Pair(n.length >= NAME_MIN_LENGTH, "Business name is too short") },
      { n -> Pair(n.length <= NAME_MAX_LENGTH, "Business name is too long") }
    )

  internal fun validateAddress(address: BusinessPlace?): ValidationResult =
    Validator.validate(
      address,
      { a -> Pair(a != null, "Address is required") },
    )

  internal fun validatePhoneNumber(phoneNumber: String): ValidationResult =
    Validator.validate(
      phoneNumber,
      { pn -> Pair(pn.isNotEmpty(), "Phone number is required") },
      { pn ->
        Pair(pn.length == PHONE_NUMBER_LENGTH, "Phone number must be $PHONE_NUMBER_LENGTH numbers")
      },
    )

  internal fun validateWebsite(website: String): ValidationResult =
    Validator.validate(
      website,
      { w -> Pair(w.isEmpty() || UrlValidator().isValid(w), "Website url is not valid") },
    )

  internal fun validateImages(images: List<Uri>): ValidationResult =
    Validator.validate(
      images,
      { i -> Pair(i.none { it == Uri.EMPTY }, "At least one image is required") },
      { i -> Pair(i.size >= MIN_IMAGES_NUMBER, "At most $MIN_IMAGES_NUMBER images are allowed") },
      { i -> Pair(i.size <= MAX_IMAGES_NUMBER, "At most $MAX_IMAGES_NUMBER images are allowed") },
    )
}
