package com.example.dora.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dora.common.Location
import com.example.dora.ui.composable.NavigationBarFunction
import com.example.dora.ui.navigation.DoraScreen
import com.example.dora.ui.navigation.NavigationGraph

@OptIn(ExperimentalMaterial3Api::class)
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
        topBar = {
            if (
                currentScreen != DoraScreen.SignIn.name && currentScreen != DoraScreen.SignUp.name
            ) {
                CenterAlignedTopAppBar(
                    title = { Text(text = currentScreen) },
                    colors =
                        TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                )
            }
        },
        bottomBar = {
            if (
                currentScreen != DoraScreen.SignIn.name && currentScreen != DoraScreen.SignUp.name
            ) {
                NavigationBarFunction(navController)
            }
        },
        floatingActionButton = {
            if (
                currentScreen != DoraScreen.SignIn.name &&
                    currentScreen != DoraScreen.SignUp.name &&
                    currentScreen != DoraScreen.Profile.name &&
                    currentScreen != DoraScreen.AddBusiness.name
            ) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { navController.navigate(DoraScreen.AddBusiness.name) }
                ) {
                    Icon(Icons.Filled.Add, "Add business")
                }
            }
        }
    ) { innerPadding ->
        NavigationGraph(
            navController = navController,
            startDestination = startDestination,
            paddingValues = innerPadding,
            location = location,
            startLocationUpdates = startLocationUpdates,
            snackbarHostState = snackbarHostState
        )
    }
}
