package com.example.dora.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dora.ui.screen.HomeScreen
import com.example.dora.ui.screen.SignInScreen
import com.example.dora.viewmodel.HomeViewModel
import com.example.dora.viewmodel.SignInViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    signInViewModel: SignInViewModel,
    homeViewModel: HomeViewModel
) {
    NavHost(
        navController = navController,
        startDestination = DoraScreen.Login.name,
        modifier = modifier
    ) {
        composable(route = DoraScreen.Home.name) {
            HomeScreen(
                homeViewModel = homeViewModel,
                onSignOut = { navController.navigate(DoraScreen.Login.name) }
            )
        }

        composable(route = DoraScreen.Login.name) {
            SignInScreen(
                signInViewModel = signInViewModel,
                onSignIn = { navController.navigate(DoraScreen.Home.name) },
                onSignUp = { navController.navigate(DoraScreen.Home.name) }
            )
        }
    }
}