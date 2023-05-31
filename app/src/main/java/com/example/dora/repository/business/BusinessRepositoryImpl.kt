package com.example.dora.repository.business

import android.net.Uri
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.dora.common.ErrorMessage
import com.example.dora.common.Location
import com.example.dora.common.SuccessMessage
import com.example.dora.model.Business
import com.example.dora.network.NetworkRequest
import com.example.dora.network.database.FirestoreAPI
import com.example.dora.network.database.FirestoreRequest
import com.example.dora.network.storage.FirebaseStorageAPI
import com.example.dora.network.storage.FirebaseStorageRequest
import com.google.firebase.firestore.Filter
import javax.inject.Inject
import kotlin.math.cos
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
        if (business.images?.isNotEmpty()!!) {
          val imagesUrls =
            storeBusinessImagesToFirebaseStorage(
              business.owner?.uid!!,
              business.uuid!!,
              *business.images!!.toTypedArray()
            )
          business.images = imagesUrls
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

  override suspend fun getBusinessesByUserId(userId: String): Either<ErrorMessage, List<Business>> =
    withContext(ioDispatcher) {
      try {
        val request =
          FirestoreRequest(
            collection = Business.collection,
            where = Filter.equalTo("owner.uid", userId)
          )

        firestoreAPI
          .find(NetworkRequest.of(request))
          .data!!
          .findTask!!
          .await()
          .toObjects(Business::class.java)
          .toList()
          .right()
      } catch (e: Exception) {
        ErrorMessage(e.message!!).left()
      }
    }

  override suspend fun getBusinessById(businessId: String): Either<ErrorMessage, Business> =
    withContext(ioDispatcher) {
      try {
        val request =
          FirestoreRequest(
            collection = Business.collection,
            where = Filter.equalTo("uuid", businessId)
          )

        firestoreAPI
          .find(NetworkRequest.of(request))
          .data!!
          .findTask!!
          .await()
          .toObjects(Business::class.java)
          .toList()
          .first()
          .right()
      } catch (e: Exception) {
        ErrorMessage(e.message!!).left()
      }
    }

  override suspend fun getBusinessesClosedToMe(
    myPosition: Location
  ): Either<ErrorMessage, List<Business>> =
    withContext(ioDispatcher) {
      try {
        val lat = getBusinessesInClosedLatitude(myPosition.latitude)
        val lon = getBusinessesInClosedLongitude(myPosition.latitude, myPosition.longitude)
        lat.intersect(lon).toList().right()
      } catch (e: Exception) {
        ErrorMessage(e.message!!).left()
      }
    }

  override suspend fun getBusinessesDefault(): Either<ErrorMessage, List<Business>> =
    withContext(ioDispatcher) {
      try {
        val request = FirestoreRequest(collection = Business.collection, where = Filter.and())

        firestoreAPI
          .find(NetworkRequest.of(request))
          .data!!
          .findTask!!
          .await()
          .toObjects(Business::class.java)
          .toList()
          .right()
      } catch (e: Exception) {
        ErrorMessage(e.message!!).left()
      }
    }

  private suspend fun getBusinessesInClosedLatitude(latitude: Double): Set<Business> {
    val request =
      FirestoreRequest(
        collection = Business.collection,
        where =
          Filter.and(
            Filter.greaterThanOrEqualTo("address.location.latitude", latitude - 0.0449),
            Filter.lessThanOrEqualTo("address.location.latitude", latitude + 0.0449),
          )
      )

    return firestoreAPI
      .find(NetworkRequest.of(request))
      .data!!
      .findTask!!
      .await()
      .toObjects(Business::class.java)
      .toSet()
  }

  private suspend fun getBusinessesInClosedLongitude(
    latitude: Double,
    longitude: Double
  ): Set<Business> {
    val request =
      FirestoreRequest(
        collection = Business.collection,
        where =
          Filter.and(
            Filter.lessThanOrEqualTo(
              "address.location.longitude",
              longitude + 0.0449 / cos(latitude)
            ),
            Filter.greaterThanOrEqualTo(
              "address.location.longitude",
              longitude - 0.0449 / cos(latitude)
            ),
          )
      )

    return firestoreAPI
      .find(NetworkRequest.of(request))
      .data!!
      .findTask!!
      .await()
      .toObjects(Business::class.java)
      .toSet()
  }

  private suspend fun storeBusinessImagesToFirebaseStorage(
    userId: String,
    businessId: String,
    vararg images: String
  ): List<String> {
    val stored = mutableListOf<String>()

    for (image in images) {
      val result =
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
      stored.add(result.toString())
    }

    return stored.toList()
  }
}
