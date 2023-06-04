package com.example.dora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dora.model.Business
import com.example.dora.ui.composable.BusinessCard
import com.example.dora.viewmodel.SearchResultsViewModel
import kotlinx.coroutines.launch

@Composable
internal fun SearchResultsScreen(
  searchResultsViewModel: SearchResultsViewModel,
  searchKey: String,
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
      searchResultsViewModel
        .searchBusinesses(searchKey)
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

  LazyColumn(
    modifier = modifier.fillMaxSize().padding(paddingValues),
    verticalArrangement = Arrangement.SpaceAround,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    if (!errorMessageHidden) {
      item {
        Text(
          text = errorMessage,
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
