package com.example.dora

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.dora.network.FirebaseRequest
import com.example.dora.network.auth.FirebaseAuthAPI
import com.example.dora.network.auth.UserCredentials
import com.example.dora.ui.theme.DoraTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var firebaseAuth: FirebaseAuthAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.firebaseAuth = FirebaseAuthAPI()

        setContent {
            DoraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current as Activity
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        var name by rememberSaveable { mutableStateOf("") }
                        Button(onClick = {
                            firebaseAuth.signUpWithEmailAndPassword(
                                FirebaseRequest(UserCredentials(
                                    "L",
                                    "Samore",
                                    "test@gmail.com",
                                    "Test123!",
                                ))
                            ).data?.addOnCompleteListener(context) { task ->
                                if (task.isSuccessful) {
                                    name = Firebase.auth.currentUser?.email!!
                                } else {
                                    name = task.exception?.message!!
                                }
                            }
                        }) {
                            Text(text = "Test register")
                        }

                        Button(onClick = {
                            firebaseAuth.signOut()
                        }) {
                            Text(text = "Test logout")
                        }

                        Button(onClick = {
                            firebaseAuth.deleteUser()
                        }) {
                            Text(text = "Test delete")
                        }

                        Text(text = "Hello $name")
                    }
                }
            }
        }
    }
}