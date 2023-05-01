package com.example.dora.di

import com.example.dora.repository.auth.AuthenticationRepositoryImpl
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    fun provideAuthenticationRepository() = AuthenticationRepositoryImpl()
}