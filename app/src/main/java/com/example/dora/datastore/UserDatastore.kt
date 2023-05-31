package com.example.dora.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserDatastore(private val context: Context) {

  val userId: Flow<String?> =
    context.datastore.data
      .catch {
        if (it is IOException) {
          it.printStackTrace()
          emit(emptyPreferences())
        } else {
          throw it
        }
      }
      .map { preferences -> preferences[USER_ID] }

  suspend fun saveUserIdToDataStore(userId: String) {
    context.datastore.edit { preferences -> preferences[USER_ID] = userId }
  }

  companion object {
    private val Context.datastore: DataStore<Preferences> by
      preferencesDataStore(name = "users_preferences")
    private val USER_ID = stringPreferencesKey(name = "user_id")
  }
}
