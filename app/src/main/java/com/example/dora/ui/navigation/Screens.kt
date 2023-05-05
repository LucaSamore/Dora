package com.example.dora.ui.navigation

sealed class NavigationScreen(val name: String) {
    object Home : NavigationScreen("home")
    object Login : NavigationScreen("login")
}