package com.example.dora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.dora.common.Location
import com.example.dora.viewmodel.HomeViewModel

@Composable
internal fun HomeScreen(
    homeViewModel: HomeViewModel,
    modifier: Modifier,
    location: MutableState<Location>,
    startLocationUpdates: () -> Unit,
) {
    var currentLocation by rememberSaveable { mutableStateOf("") }

    startLocationUpdates()

    homeViewModel.updateLocation(location.value)

    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home screen", style = MaterialTheme.typography.titleLarge)

        Button(onClick = { currentLocation = location.value.toString() }) {
            Text(text = "Get location")
        }

        Text(text = currentLocation, textAlign = TextAlign.Center)
    }
}

@Preview(showBackground = true)
@Composable
internal fun HomeScreenPreview() {
    // HomeScreen()
}
