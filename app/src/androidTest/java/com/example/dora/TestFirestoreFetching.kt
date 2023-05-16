package com.example.dora

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.dora.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestFirestoreFetching {

    private lateinit var db: FirebaseFirestore

    @Before
    fun beforeTests() {
        Firebase.initialize(InstrumentationRegistry.getInstrumentation().targetContext)
        db = Firebase.firestore
    }

    @Test
    fun testUserFetching() = runBlocking {
        val docRef = db.collection(User.collection).document("iafllg0hi6ahUkOrbCHoqz0vVzw2")

        try {
            val res = docRef.get().await().data
            Log.i("user", res?.get("createdAt").toString())
        } catch (e: Exception) {
            println(e.message)
        }

        return@runBlocking
    }
}