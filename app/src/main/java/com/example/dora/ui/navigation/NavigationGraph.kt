package com.example.dora.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dora.common.Location
import com.example.dora.ui.screen.HomeScreen
import com.example.dora.ui.screen.ProfileScreen
import com.example.dora.ui.screen.SignInScreen
import com.example.dora.ui.screen.SignUpScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    location: MutableState<Location>,
    snackbarHostState: SnackbarHostState,
    startLocationUpdates: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = DoraScreen.Home.name) {
            HomeScreen(
                homeViewModel = hiltViewModel(),
                modifier = modifier,
                location = location,
                startLocationUpdates = startLocationUpdates,
                onSignOut = {
                    navController.navigate(DoraScreen.SignIn.name) {
                        popUpTo(DoraScreen.Home.name) { inclusive = true }
                    }
                },
            )
        }

        composable(route = DoraScreen.SignIn.name) {
            SignInScreen(
                signInViewModel = hiltViewModel(),
                onSignIn = {
                    navController.navigate(DoraScreen.Home.name) {
                        popUpTo(DoraScreen.SignIn.name) { inclusive = true }
                    }
                },
                onSignUp = { navController.navigate(DoraScreen.SignUp.name) },
                modifier = modifier,
            )
        }

        composable(route = DoraScreen.SignUp.name) {
            SignUpScreen(
                signUpViewModel = hiltViewModel(),
                modifier = modifier,
                onSignUp = {
                    navController.backQueue.clear()
                    navController.navigate(DoraScreen.Home.name)
                },
                onBackToSignIn = { navController.navigate(DoraScreen.SignIn.name) },
            )
        }

        composable(route = DoraScreen.Profile.name) {
            ProfileScreen(
                modifier = modifier,
                profileViewModel = hiltViewModel(),
                onError = { navController.navigate(DoraScreen.Home.name) },
                onUpdate = { navController.navigate(DoraScreen.Home.name) }
            )
        }
    }
}
