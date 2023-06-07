package com.example.dora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dora.ui.composable.BusinessCard
import com.example.dora.viewmodel.MyBusinessesViewModel

@Composable
internal fun MyBusinessesScreen(
  myBusinessesViewModel: MyBusinessesViewModel,
  navController: NavHostController,
  modifier: Modifier,
  paddingValues: PaddingValues,
) {
  val context = LocalContext.current
  myBusinessesViewModel.getMyBusinesses()
  val businesses by myBusinessesViewModel.myBusinesses.collectAsState()

  Column(
    modifier = modifier.fillMaxSize().padding(paddingValues),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (!myBusinessesViewModel.errorMessageHidden.value) {
      Text(
        text = myBusinessesViewModel.errorMessage.value,
        color = Color.Red,
        modifier = modifier.padding(top = 4.dp, bottom = 6.dp),
        textAlign = TextAlign.Center
      )
    }

    Spacer(modifier = modifier.size(12.dp))

    LazyColumn {
      items(businesses) { business -> BusinessCard(business, context, modifier, navController) }
    }
  }
}
