package com.example.dora.di

import com.example.dora.datastore.UserDatastore
import com.example.dora.network.auth.FirebaseAuthAPI
import com.example.dora.network.database.FirestoreAPI
import com.example.dora.network.storage.FirebaseStorageAPI
import com.example.dora.repository.auth.AuthenticationRepository
import com.example.dora.repository.auth.FirebaseAuthRepository
import com.example.dora.repository.business.BusinessRepository
import com.example.dora.repository.business.BusinessRepositoryImpl
import com.example.dora.repository.user.UserRepository
import com.example.dora.repository.user.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @FirebaseRepository
    @Singleton
    @Provides
    fun providesAuthenticationRepository(
        firebaseAuthAPI: FirebaseAuthAPI,
        firestoreAPI: FirestoreAPI,
        firebaseStorageAPI: FirebaseStorageAPI,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        userDatastore: UserDatastore
    ): AuthenticationRepository =
        FirebaseAuthRepository(
            firebaseAuthAPI = firebaseAuthAPI,
            firestoreAPI = firestoreAPI,
            firebaseStorageAPI = firebaseStorageAPI,
            ioDispatcher = ioDispatcher,
            userDatastore = userDatastore
        )

    @FirebaseRepository
    @Singleton
    @Provides
    fun providesUserRepository(
        firebaseAuthAPI: FirebaseAuthAPI,
        firestoreAPI: FirestoreAPI,
        firebaseStorageAPI: FirebaseStorageAPI,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        userDatastore: UserDatastore,
    ): UserRepository =
        UserRepositoryImpl(
            firebaseAuthAPI = firebaseAuthAPI,
            firestoreAPI = firestoreAPI,
            firebaseStorageAPI = firebaseStorageAPI,
            ioDispatcher = ioDispatcher,
            userDatastore = userDatastore
        )

    @FirebaseRepository
    @Singleton
    @Provides
    fun providesBusinessRepository(
        firestoreAPI: FirestoreAPI,
        firebaseStorageAPI: FirebaseStorageAPI,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): BusinessRepository =
        BusinessRepositoryImpl(
            firestoreAPI = firestoreAPI,
            firebaseStorageAPI = firebaseStorageAPI,
            ioDispatcher = ioDispatcher,
        )
}

@Retention(AnnotationRetention.BINARY) @Qualifier annotation class FirebaseRepository
