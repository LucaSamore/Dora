package com.example.dora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
internal fun WriteReviewScreen(
  businessId: String,
  modifier: Modifier,
  paddingValues: PaddingValues,
  onSuccess: () -> Unit,
) {
  Column(
    modifier = modifier.fillMaxSize().padding(paddingValues),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(text = "Write Review Screen")
    Text(text = businessId)
  }
}
