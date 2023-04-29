package com.example.dora.di

import android.content.Context
import com.example.dora.repository.auth.AuthenticationRepositoryImpl
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    fun provideAuthenticationRepository(@ApplicationContext context: Context) = AuthenticationRepositoryImpl(context)
}