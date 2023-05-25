package com.example.dora.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import arrow.core.Either
import arrow.core.left
import com.example.dora.common.BusinessPlace
import com.example.dora.common.ErrorMessage
import com.example.dora.common.SuccessMessage
import com.example.dora.di.FirebaseRepository
import com.example.dora.di.IoDispatcher
import com.example.dora.model.Business
import com.example.dora.model.Category
import com.example.dora.repository.business.BusinessRepository
import com.example.dora.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltViewModel
class AddBusinessViewModel
@Inject
constructor(
    @FirebaseRepository private val userRepository: UserRepository,
    @FirebaseRepository private val businessRepository: BusinessRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    suspend fun createBusiness(
        name: String,
        description: String?,
        address: BusinessPlace,
        website: String?,
        phoneNumber: String?,
        category: Category,
        isOpen: Boolean,
        images: List<Uri>,
    ): Either<ErrorMessage, SuccessMessage> =
        when (val user = userRepository.getUser()) {
            is Either.Left -> user.value.left()
            is Either.Right -> {
                val business =
                    Business(
                        uuid = UUID.randomUUID().toString(),
                        name = name,
                        owner = user.value,
                        description = description,
                        address = address,
                        website = website,
                        phoneNumber = phoneNumber,
                        category = category,
                        isOpen = isOpen,
                        images = images.map { it.toString() }
                    )
                storeBusiness(business)
            }
        }

    private suspend fun storeBusiness(business: Business) =
        withContext(ioDispatcher) { businessRepository.storeBusiness(business) }
}
