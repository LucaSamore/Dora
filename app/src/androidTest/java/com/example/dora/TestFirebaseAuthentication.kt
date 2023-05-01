package com.example.dora

import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.junit.Before
import org.junit.Test

class TestFirebaseAuthentication {

    // TODO: Fix this test

    @Before
    fun beforeTests() {
        FirebaseApp.initializeApp(InstrumentationRegistry.getInstrumentation().targetContext)
        Firebase.auth.useEmulator("10.0.2.2", 9099)
    }

    @Test
    fun testSignUpWithEmailAndPassword() {
        Firebase.auth
            .createUserWithEmailAndPassword("test@gmail.com", "Test123!")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println(Firebase.auth.currentUser?.email)
                    println("Account created successfully")
                } else {
                    println("Account not created")
                }
            }
    }
}