package com.example.dora.common.validation

object ReviewValidator {
  private const val MIN_RATING = 1
  private const val MAX_RATING = 5

  internal fun validateContent(content: String): ValidationResult =
    Validator.validate(
      content,
      { c -> Pair(c.isNotEmpty(), "Content is required") },
    )

  internal fun validateRating(rating: Int): ValidationResult =
    Validator.validate(
      rating,
      { r ->
        Pair(r in MIN_RATING..MAX_RATING, "Rating must be between $MIN_RATING and $MAX_RATING")
      }
    )
}
