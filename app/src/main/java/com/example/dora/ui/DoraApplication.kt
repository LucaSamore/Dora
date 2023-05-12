package com.example.dora.ui

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.dora.common.Location
import com.example.dora.ui.navigation.NavigationGraph

@Composable
internal fun DoraApplication(
    startDestination: String,
    location: MutableState<Location>,
    startLocationUpdates: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = {
            NavigationGraph(
                navController = rememberNavController(),
                startDestination = startDestination,
                location = location,
                startLocationUpdates = startLocationUpdates,
            )
        }
    )
}
