package com.example.dora.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.dora.ui.navigation.DoraScreen
import com.example.dora.ui.navigation.NavigationGraph
import com.example.dora.ui.theme.DoraTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var startDestination: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // TODO: Make this check firebase independent
                    val currentUser = Firebase.auth.currentUser
                    startDestination = if (currentUser != null) {
                        DoraScreen.Home.name
                    } else {
                        DoraScreen.SignIn.name
                    }
                    NavigationGraph(
                        navController = rememberNavController(),
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}