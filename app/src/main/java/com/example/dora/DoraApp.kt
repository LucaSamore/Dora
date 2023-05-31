package com.example.dora

import android.app.Application
import com.example.dora.database.DoraDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DoraApp : Application() {
  val database by lazy { DoraDatabase.getDatabase(this) }
}
