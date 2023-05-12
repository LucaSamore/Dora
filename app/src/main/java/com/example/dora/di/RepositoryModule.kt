package com.example.dora.di

import com.example.dora.datastore.UserDatastore
import com.example.dora.repository.auth.AuthenticationRepository
import com.example.dora.repository.auth.FirebaseAuthRepository
import com.example.dora.repository.user.UserRepository
import com.example.dora.repository.user.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @FirebaseRepository
    @Singleton
    @Provides
    fun providesAuthenticationRepository(userDatastore: UserDatastore): AuthenticationRepository = FirebaseAuthRepository(userDatastore = userDatastore)

    @FirebaseRepository
    @Singleton
    @Provides
    fun providesUserRepository(): UserRepository = UserRepositoryImpl()
}

@Retention(AnnotationRetention.BINARY) @Qualifier annotation class FirebaseRepository
