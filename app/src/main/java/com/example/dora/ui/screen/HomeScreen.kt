package com.example.dora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home Screen")

        Spacer(modifier = Modifier.padding(vertical = 12.dp))

        Button(onClick = { onSignOut() }) {
            Text(text = "Sign Out")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // HomeScreen()
}