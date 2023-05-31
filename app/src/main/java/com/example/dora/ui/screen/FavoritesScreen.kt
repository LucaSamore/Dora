package com.example.dora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dora.model.Business
import com.example.dora.ui.composable.BusinessCard
import com.example.dora.viewmodel.FavoriteViewModel
import kotlinx.coroutines.launch

@Composable
internal fun FavoritesScreen(
    favoriteViewModel: FavoriteViewModel,
    modifier: Modifier,
    paddingValues: PaddingValues,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val businesses = remember { mutableStateListOf<Business>() }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var errorMessageHidden by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        scope.launch {
            favoriteViewModel
                .getBusinesses()
                .fold(
                    { left ->
                        errorMessage = left.message
                        errorMessageHidden = false
                    },
                    { right ->
                        businesses.apply {
                            clear()
                            addAll(right)
                        }
                    }
                )
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(paddingValues),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!errorMessageHidden) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = modifier.padding(top = 24.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
        }

        if (businesses.isEmpty()) {
            Text(
                text = "No favorites yet",
                modifier = modifier.padding(top = 24.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
        }

        LazyColumn {
            items(businesses) { business ->
                BusinessCard(business, context, modifier, navController)
            }
        }
    }
}
