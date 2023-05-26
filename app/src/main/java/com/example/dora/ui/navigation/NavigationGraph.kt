package com.example.dora.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dora.common.Location
import com.example.dora.ui.screen.*
import com.example.dora.ui.screen.SettingsScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String,
    paddingValues: PaddingValues,
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
                paddingValues = paddingValues,
                modifier = modifier,
                location = location,
                startLocationUpdates = startLocationUpdates,
                navController = navController,
            )
        }

        composable(route = DoraScreen.SignIn.name) {
            SignInScreen(
                signInViewModel = hiltViewModel(),
                paddingValues = paddingValues,
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
                paddingValues = paddingValues,
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
                paddingValues = paddingValues,
                profileViewModel = hiltViewModel(),
                onError = { navController.navigate(DoraScreen.Home.name) },
                onUpdate = { navController.navigate(DoraScreen.Home.name) },
            )
        }

        composable(route = DoraScreen.Settings.name) {
            SettingsScreen(
                settingsViewModel = hiltViewModel(),
                paddingValues = paddingValues,
                modifier = modifier,
                onSignOut = {
                    navController.navigate(DoraScreen.SignIn.name) {
                        popUpTo(DoraScreen.Home.name) { inclusive = true }
                    }
                },
                onDismiss = { navController.navigate(DoraScreen.Settings.name) },
                onAccountDeleted = { navController.navigate(DoraScreen.SignIn.name) },
                onMyBusinessesClicked = {
                    navController.navigate(DoraScreen.MyBusinesses.name) {
                        popUpTo(DoraScreen.MyBusinesses.name) { inclusive = true }
                    }
                }
            )
        }

        composable(route = DoraScreen.AddBusiness.name) {
            AddBusinessScreen(
                addBusinessViewModel = hiltViewModel(),
                paddingValues = paddingValues,
                modifier = modifier,
                onCreated = {
                    navController.navigate(DoraScreen.Home.name) {
                        popUpTo(DoraScreen.Home.name) { inclusive = true }
                    }
                },
            )
        }

        composable(route = DoraScreen.MyBusinesses.name) {
            MyBusinessesScreen(
                myBusinessesViewModel = hiltViewModel(),
                navController = navController,
                modifier = modifier,
                paddingValues = paddingValues,
            )
        }

        composable(route = "${DoraScreen.BusinessDetails.name}/{businessId}") {
            BusinessDetailsScreen(
                businessDetailsViewModel = hiltViewModel(),
                businessId = it.arguments?.getString("businessId")!!,
                modifier = modifier
            )
        }
    }
}
