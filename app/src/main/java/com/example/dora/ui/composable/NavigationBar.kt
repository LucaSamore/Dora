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

@Composable
fun NavigationBarFunction(

) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text(text = "Home") },
            selected = false,
            onClick = { TODO() }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text(text = "Profile") },
            selected = false,
            onClick = { TODO() }
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
            onClick = { TODO() }
        )
    }
}