package com.example.dora

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestFirebaseAuthentication {

    private lateinit var auth: FirebaseAuth

    @Before
    fun beforeTests() {
        Firebase.initialize(InstrumentationRegistry.getInstrumentation().targetContext)
        auth = Firebase.auth
        auth.useEmulator("10.0.2.2", 9099)
    }

    @Test
    fun testSignUpWithEmailAndPassword() {
        runBlocking {
            auth
                .createUserWithEmailAndPassword("test@gmail.com", "Test123!")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println(Firebase.auth.currentUser?.email)
                        println("Account created successfully")
                    } else {
                        println("Account not created")
                    }
                }.await()
        }
    }
}