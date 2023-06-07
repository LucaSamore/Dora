package com.example.dora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dora.ui.composable.BusinessCard
import com.example.dora.viewmodel.SearchResultsViewModel

@Composable
internal fun SearchResultsScreen(
  searchResultsViewModel: SearchResultsViewModel,
  searchKey: String,
  modifier: Modifier,
  paddingValues: PaddingValues,
  navController: NavHostController,
) {
  searchResultsViewModel.searchBusinesses(searchKey)
  val context = LocalContext.current
  val businesses by searchResultsViewModel.searchResult.collectAsState()

  LazyColumn(
    modifier = modifier.fillMaxSize().padding(paddingValues),
    verticalArrangement = Arrangement.SpaceAround,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    if (!searchResultsViewModel.errorMessageHidden.value) {
      item {
        Text(
          text = searchResultsViewModel.errorMessage.value,
          color = Color.Red,
          modifier = modifier.padding(top = 4.dp, bottom = 6.dp),
          textAlign = TextAlign.Center
        )
      }
    }

    if (businesses.isEmpty()) {
      item {
        Text(
          text = "No results found",
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.bodyLarge
        )
      }
    }

    items(businesses) { business -> BusinessCard(business, context, modifier, navController) }
  }
}
