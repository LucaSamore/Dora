package com.example.dora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.dora.viewmodel.FavoriteViewModel

@Composable
internal fun FavoritesScreen(
    favoriteViewModel: FavoriteViewModel,
    modifier: Modifier,
    paddingValues: PaddingValues,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(paddingValues),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Ciao bella")
    }
}
