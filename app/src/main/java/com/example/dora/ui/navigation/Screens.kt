package com.example.dora.ui.navigation

sealed class DoraScreen(val name: String) {
    object Home : DoraScreen("home")
    object SignIn : DoraScreen("sign in")
    object SignUp : DoraScreen("sign up")
}