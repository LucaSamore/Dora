package com.example.dora.di

import com.example.dora.network.auth.FirebaseAuthAPI
import com.example.dora.network.database.FirestoreAPI
import com.example.dora.network.storage.FirebaseStorageAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

  @Provides fun providesFirebaseAuthApi() = FirebaseAuthAPI()

  @Provides fun providesFirestoreApi() = FirestoreAPI()

  @Provides fun providesFirebaseStorageApi() = FirebaseStorageAPI()
}
