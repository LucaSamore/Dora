package com.example.dora.ui.navigation

sealed class DoraScreen(val name: String) {
    object Home : DoraScreen("home")
    object Login : DoraScreen("login")
}