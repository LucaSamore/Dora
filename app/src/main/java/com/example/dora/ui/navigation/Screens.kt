package com.example.dora.ui.navigation

sealed class DoraScreen(val name: String) {
    object Home : DoraScreen("Home")
    object SignIn : DoraScreen("Sign In")
    object SignUp : DoraScreen("Sign Up")
    object Profile : DoraScreen("Profile")
    object Settings : DoraScreen("Settings")
}
