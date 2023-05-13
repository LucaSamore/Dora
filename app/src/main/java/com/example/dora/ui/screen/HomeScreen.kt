package com.example.dora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import arrow.core.Either
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dora.common.Location
import com.example.dora.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onSignOut: () -> Unit,
    modifier: Modifier,
    location: MutableState<Location>,
    startLocationUpdates: () -> Unit
) {
    val context = LocalContext.current
    var profilePicture by remember { mutableStateOf<Any?>(null) }
    var error by rememberSaveable { mutableStateOf("Nothing yet") }
    val scope = rememberCoroutineScope()

    startLocationUpdates()

    homeViewModel.updateLocation(location.value)

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home screen")

        Spacer(modifier = modifier.padding(vertical = 12.dp))

        Button(
            onClick = {
                homeViewModel.signOut()
                onSignOut()
            }
        ) {
            Text(text = "Sign Out")
        }

        Spacer(modifier = modifier.padding(vertical = 12.dp))

        Button(
            onClick = {
                scope.launch {
                    when (val result = homeViewModel.deleteAccount()) {
                        is Either.Left -> error = result.value.message
                        is Either.Right -> onSignOut()
                    }
                }
            }
        ) {
            Text(text = "Delete account")
        }

        Spacer(modifier = modifier.padding(vertical = 12.dp))

        Button(onClick = { error = location.value.toString() }) { Text(text = "Get location") }

        Spacer(modifier = modifier.padding(vertical = 12.dp))

        Text(text = error)

        Button(onClick = {
            TODO()
        }) { Text(text = "Download profile picture") }

        AsyncImage(
            model =
            ImageRequest.Builder(context).data(profilePicture).crossfade(true).build(),
            contentDescription = "profile picture",
            modifier = Modifier.size(256.dp, 256.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // HomeScreen()
}
