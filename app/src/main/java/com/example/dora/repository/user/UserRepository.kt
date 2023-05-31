package com.example.dora.repository.user

import android.net.Uri
import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.common.Location
import com.example.dora.common.SuccessMessage
import com.example.dora.model.User

interface UserRepository {
  suspend fun getUser(): Either<ErrorMessage, User>

  suspend fun updateUser(user: User): Either<ErrorMessage, SuccessMessage>

  suspend fun updateAuthEmailAddress(newEmailAddress: String): Either<ErrorMessage, SuccessMessage>

  suspend fun updateAuthPassword(newPlainPassword: String): Either<ErrorMessage, SuccessMessage>

  suspend fun updateLocation(location: Location): Either<ErrorMessage, SuccessMessage>

  suspend fun updateProfilePicture(
    userId: String,
    profilePictureUri: Uri
  ): Either<ErrorMessage, Uri>
}
