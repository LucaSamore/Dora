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
import com.example.dora.network.NetworkRequest
import com.example.dora.network.auth.FirebaseAuthAPI
import com.example.dora.network.auth.Credentials
import com.example.dora.network.database.FirestoreAPI
import com.example.dora.network.database.FirestoreRequest
import com.example.dora.ui.theme.DoraTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var firebaseAuth: FirebaseAuthAPI
    private lateinit var firestoreAPI: FirestoreAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.firebaseAuth = FirebaseAuthAPI()
        this.firestoreAPI = FirestoreAPI()

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
                        val request: NetworkRequest<Credentials> = NetworkRequest(Credentials.Register(
                            "luca.samore@gmail.com",
                            "Test123!",
                            "Luca",
                            "Samore",
                        ))
                        Button(onClick = {
                            firebaseAuth.signUpWithEmailAndPassword(
                                request
                            ).data?.addOnCompleteListener(context) { task ->
                                if (task.isSuccessful) {
                                    name = Firebase.auth.currentUser?.email!!
                                    firestoreAPI.insert(
                                        NetworkRequest(FirestoreRequest(request.body, "test")),
                                        autogenerateDocumentId = true
                                    )
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
                            firebaseAuth.signInWithEmailAndPassword(
                                NetworkRequest(
                                    Credentials.Login(
                                        "luca.samore@gmail.com",
                                        "Test123!",
                                ))
                            ).data?.addOnCompleteListener(context) {task ->
                                if (task.isSuccessful) {
                                    name = Firebase.auth.currentUser?.uid!!
                                } else {
                                    name = task.exception?.message!!
                                }
                            }
                        }) {
                            Text(text = "Test login")
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