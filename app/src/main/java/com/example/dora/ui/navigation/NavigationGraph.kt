package com.example.dora.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dora.ui.screen.HomeScreen
import com.example.dora.ui.screen.SignInScreen
import com.example.dora.ui.screen.SignUpScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = DoraScreen.SignIn.name,
        modifier = modifier
    ) {
        composable(route = DoraScreen.Home.name) {
            HomeScreen(
                homeViewModel = hiltViewModel(),
                onSignOut = { navController.navigate(DoraScreen.SignIn.name) }
            )
        }

        composable(route = DoraScreen.SignIn.name) {
            SignInScreen(
                signInViewModel = hiltViewModel(),
                onSignIn = { navController.navigate(DoraScreen.Home.name) },
                onSignUp = { navController.navigate(DoraScreen.SignUp.name) }
            )
        }

        composable(route = DoraScreen.SignUp.name) {
            SignUpScreen(
                signUpViewModel = hiltViewModel(),
                onSignUp = { navController.navigate(DoraScreen.Home.name) },
                onBackToSignIn = { navController.navigate(DoraScreen.SignIn.name) }
            )
        }
    }
}