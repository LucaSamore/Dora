package com.example.dora.di

import com.example.dora.repository.auth.AuthenticationRepository
import com.example.dora.repository.auth.FirebaseAuthRepository
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
    fun providesAuthenticationRepository() : AuthenticationRepository = FirebaseAuthRepository()
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class FirebaseRepository