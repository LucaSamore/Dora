package com.example.dora.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dora.common.Location
import com.example.dora.ui.navigation.DoraScreen
import com.example.dora.ui.navigation.NavigationGraph
import androidx.compose.runtime.getValue
import com.example.dora.ui.composable.NavigationBarFunction

@Composable
internal fun DoraApplication(
    startDestination: String,
    location: MutableState<Location>,
    startLocationUpdates: () -> Unit
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route ?: DoraScreen.SignIn.name

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (currentScreen != DoraScreen.SignIn.name && currentScreen != DoraScreen.SignUp.name) {
                NavigationBarFunction()
            }
        },
        floatingActionButton = {
            if (currentScreen != DoraScreen.SignIn.name && currentScreen != DoraScreen.SignUp.name) {
                FloatingActionButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Add, "Add business")
                }
            }
        }
    ) {
        NavigationGraph(
            navController = navController,
            startDestination = startDestination,
            location = location,
            startLocationUpdates = startLocationUpdates,
        )
    }
}