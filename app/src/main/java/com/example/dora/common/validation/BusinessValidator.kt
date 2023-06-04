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

  internal fun validateName(name: String): Validator.Result =
    Validator.validate(
      name,
      Validator.Rule(test = { name.isNotEmpty() }, errorMessage = "Business name is required"),
      Validator.Rule(
        test = { name.length >= NAME_MIN_LENGTH },
        errorMessage = "Business name is too short"
      ),
      Validator.Rule(
        test = { name.length <= NAME_MAX_LENGTH },
        errorMessage = "Business name is too long"
      ),
    )

  internal fun validateAddress(address: BusinessPlace?): Validator.Result =
    Validator.validate(
      address,
      Validator.Rule(test = { address != null }, errorMessage = "Address is required"),
    )

  internal fun validatePhoneNumber(phoneNumber: String): Validator.Result =
    Validator.validate(
      phoneNumber,
      Validator.Rule(
        test = { phoneNumber.isNotEmpty() },
        errorMessage = "Phone number is required"
      ),
      Validator.Rule(
        test = { phoneNumber.length == PHONE_NUMBER_LENGTH },
        errorMessage = "Phone number must be $PHONE_NUMBER_LENGTH numbers"
      ),
    )

  internal fun validateWebsite(website: String): Validator.Result =
    Validator.validate(
      website,
      Validator.Rule(
        test = { website.isEmpty() || UrlValidator().isValid(website) },
        errorMessage = "Website url is not valid"
      ),
    )

  internal fun validateImages(images: List<Uri>): Validator.Result =
    Validator.validate(
      images,
      Validator.Rule(
        test = { images.none { it == Uri.EMPTY } },
        errorMessage = "At least one image is required"
      ),
      Validator.Rule(
        test = { images.size >= MIN_IMAGES_NUMBER },
        errorMessage = "At least $MIN_IMAGES_NUMBER images are allowed"
      ),
      Validator.Rule(
        test = { images.size <= MAX_IMAGES_NUMBER },
        errorMessage = "At most $MAX_IMAGES_NUMBER images are allowed"
      ),
    )
}
