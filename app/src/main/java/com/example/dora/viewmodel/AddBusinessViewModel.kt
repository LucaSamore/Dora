package com.example.dora.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.dora.common.BusinessPlace
import com.example.dora.di.FirebaseRepository
import com.example.dora.di.IoDispatcher
import com.example.dora.model.Category
import com.example.dora.repository.business.BusinessRepository
import com.example.dora.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

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
    ) {
        TODO()
    }
}
