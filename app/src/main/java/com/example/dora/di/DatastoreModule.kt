package com.example.dora.di

import android.content.Context
import com.example.dora.datastore.UserDatastore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Singleton
    @Provides
    fun providesUserDatastore(@ApplicationContext context: Context) = UserDatastore(context)
}
