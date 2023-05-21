package com.example.dora.ui.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dora.ui.navigation.DoraScreen

@Composable
internal fun NavigationBarFunction(navController: NavHostController) {
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentScreen = backStackEntry?.destination?.route ?: DoraScreen.SignIn.name

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text(text = "Home") },
            selected = currentScreen == DoraScreen.Home.name,
            onClick = {
                navController.navigate(DoraScreen.Home.name) {
                    popUpTo(DoraScreen.Home.name) { inclusive = true }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text(text = "Profile") },
            selected = currentScreen == DoraScreen.Profile.name,
            onClick = {
                navController.navigate(DoraScreen.Profile.name) {
                    popUpTo(DoraScreen.Profile.name) { inclusive = true }
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Star, contentDescription = "Favorites") },
            label = { Text(text = "Favorites") },
            selected = false,
            onClick = { TODO() }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text(text = "Settings") },
            selected = false,
            onClick = {
                navController.navigate(DoraScreen.Settings.name) {
                    popUpTo(DoraScreen.Settings.name) { inclusive = true }
                }
            }
        )
    }
}
