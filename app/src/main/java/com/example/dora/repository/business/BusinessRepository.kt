package com.example.dora.repository.business

import arrow.core.Either
import com.example.dora.common.ErrorMessage
import com.example.dora.common.SuccessMessage
import com.example.dora.model.Business

interface BusinessRepository {
    suspend fun storeBusiness(business: Business): Either<ErrorMessage, SuccessMessage>

    suspend fun getBusinessesByUserId(userId: String): Either<ErrorMessage, List<Business>>
}
