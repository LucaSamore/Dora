package com.example.dora.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dora.ui.screen.HomeScreen
import com.example.dora.ui.screen.SignInScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = DoraScreen.Login.name,
        modifier = modifier
    ) {
        composable(route = DoraScreen.Home.name) {
            HomeScreen(
                onSignOut = { navController.navigate(DoraScreen.Login.name) }
            )
        }

        composable(route = DoraScreen.Login.name) {
            SignInScreen(
                onSignIn = { navController.navigate(DoraScreen.Home.name) },
                onSignUp = { navController.navigate(DoraScreen.Home.name) }
            )
        }
    }
}