package com.example.dora.di

import com.example.dora.repository.auth.AuthenticationRepository
import com.example.dora.repository.auth.AuthenticationRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideAuthenticationRepository() : AuthenticationRepository = AuthenticationRepositoryImpl()
}