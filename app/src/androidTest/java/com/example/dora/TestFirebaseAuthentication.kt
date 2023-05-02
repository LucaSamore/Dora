package com.example.dora

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
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
        auth.useEmulator("0.0.0.0", 9099)
    }

    @Test
    fun testSignUpWithEmailAndPassword() = runBlocking {
        auth
            .createUserWithEmailAndPassword("test@gmail.com", "Test123!")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("register", "Account created successfully")
                } else {
                    Log.i("register", "Account creation failed")
                }
            }.await()
        assert(auth.currentUser != null)
        auth.signOut()
        assert(auth.currentUser == null)
    }

    @Test
    fun testSignInWithEmailAndPassword() = runBlocking {
        auth
            .signInWithEmailAndPassword("test@gmail.com", "Test123!")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("login", "Login successful")
                } else {
                    Log.i("login", "Login failed")
                }
            }.await()
        assert(auth.currentUser != null)
    }

    @Test
    fun testDeleteAccount() = runBlocking {
        auth.currentUser?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i("delete", "Account deleted successfully")
            } else {
                Log.i("delete", "Account deletion failed")
            }
        }?.await()
        assert(auth.currentUser == null)
    }

    @After
    fun afterTests() = runBlocking {
        if (auth.currentUser != null) {
            auth.currentUser?.delete()?.await()
        }
    }

}