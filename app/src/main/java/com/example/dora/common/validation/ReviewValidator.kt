package com.example.dora.common.validation

object ReviewValidator {
  private const val MIN_RATING = 1
  private const val MAX_RATING = 5

  internal fun validateContent(content: String): Validator.Result =
    Validator.validate(
      content,
      Validator.Rule(test = { content.isNotEmpty() }, errorMessage = "Content is required")
    )

  internal fun validateRating(rating: Int): Validator.Result =
    Validator.validate(
      rating,
      Validator.Rule(
        test = { rating in MIN_RATING..MAX_RATING },
        errorMessage = "Rating must be between $MIN_RATING and $MAX_RATING"
      )
    )
}
