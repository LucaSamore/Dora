package com.example.dora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.dora.model.Business

@Composable
internal fun SearchResultsScreen(
  searchKey: String,
  modifier: Modifier,
  paddingValues: PaddingValues,
  navController: NavHostController,
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val businesses = remember { mutableStateListOf<Business>() }

  Column(
    modifier = modifier.fillMaxSize().padding(paddingValues),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(text = "Search results screen")
    Text(text = searchKey)
  }
}
