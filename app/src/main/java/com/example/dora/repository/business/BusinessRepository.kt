package com.example.dora.repository.business

import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.common.Location
import com.example.dora.common.SuccessMessage
import com.example.dora.model.Business

interface BusinessRepository {
  suspend fun storeBusiness(business: Business): Either<ErrorMessage, SuccessMessage>

  suspend fun getBusinessesByUserId(userId: String): Either<ErrorMessage, List<Business>>

  suspend fun getBusinessById(businessId: String): Either<ErrorMessage, Business>

  suspend fun getBusinessesClosedToMe(myPosition: Location): Either<ErrorMessage, List<Business>>

  suspend fun getBusinessesDefault(): Either<ErrorMessage, List<Business>>

  suspend fun getBusinessesByName(searchKey: String): Either<ErrorMessage, List<Business>>
}
