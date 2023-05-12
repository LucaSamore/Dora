package com.example.dora.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserDatastore(private val context: Context) {

    val userId: Flow<String?> = context.datastore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[USER_ID]
    }

    val profilePictureFileName: Flow<String?> = context.datastore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[PROFILE_PICTURE_FILE_NAME]
    }

    suspend fun saveUserIdToDataStore(userId: String) {
        context.datastore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    suspend fun saveProfilePictureFileNameToDataStore(profilePictureFileName: String) {
        context.datastore.edit { preferences ->
            preferences[PROFILE_PICTURE_FILE_NAME] = profilePictureFileName
        }
    }

    companion object {
        private val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = "users_preferences")
        private val USER_ID = stringPreferencesKey(name = "user_id")
        private val PROFILE_PICTURE_FILE_NAME = stringPreferencesKey(name = "profile_picture")
    }
}