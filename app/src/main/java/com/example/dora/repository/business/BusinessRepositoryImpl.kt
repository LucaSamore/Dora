package com.example.dora.repository.business

import android.net.Uri
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.dora.common.ErrorMessage
import com.example.dora.common.SuccessMessage
import com.example.dora.model.Business
import com.example.dora.network.NetworkRequest
import com.example.dora.network.database.FirestoreAPI
import com.example.dora.network.database.FirestoreRequest
import com.example.dora.network.storage.FirebaseStorageAPI
import com.example.dora.network.storage.FirebaseStorageRequest
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BusinessRepositoryImpl
@Inject
constructor(
    private val firestoreAPI: FirestoreAPI,
    private val firebaseStorageAPI: FirebaseStorageAPI,
    private val ioDispatcher: CoroutineDispatcher,
) : BusinessRepository {

    override suspend fun storeBusiness(business: Business): Either<ErrorMessage, SuccessMessage> =
        withContext(ioDispatcher) {
            try {
                if (business.images != null) {
                    storeBusinessImagesToFirebaseStorage(
                        business.owner?.uid!!,
                        business.uuid!!,
                        *business.images.toTypedArray()
                    )
                }

                val request =
                    FirestoreRequest(
                        data = business,
                        collection = Business.collection,
                        document = business.uuid
                    )

                firestoreAPI.insert(NetworkRequest.of(request)).data?.insertTask?.await().let {
                    SuccessMessage("Business created successfully").right()
                }
            } catch (e: Exception) {
                ErrorMessage(e.message!!).left()
            }
        }

    private suspend fun storeBusinessImagesToFirebaseStorage(
        userId: String,
        businessId: String,
        vararg images: String
    ) {
        for (image in images) {
            firebaseStorageAPI
                .uploadFile(
                    NetworkRequest.of(
                        FirebaseStorageRequest(
                            fileUri = Uri.parse(image),
                            reference = "$userId/businesses/$businessId"
                        )
                    )
                )
                .data
                ?.await()
        }
    }
}
