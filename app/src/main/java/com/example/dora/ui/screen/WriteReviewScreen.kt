package com.example.dora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun WriteReviewScreen(
  businessId: String,
  modifier: Modifier,
  paddingValues: PaddingValues,
  onSuccess: () -> Unit,
) {

  var content by rememberSaveable { mutableStateOf("") }
  var sliderPosition by remember { mutableStateOf(1f) }

  Column(
    modifier = modifier.fillMaxSize().padding(paddingValues).verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.SpaceAround,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Column(
      modifier = modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      Text(
        text = "Review",
        modifier = modifier.fillMaxWidth().padding(12.dp),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      OutlinedTextField(
        value = content,
        onValueChange = { content = it },
        modifier = modifier.fillMaxWidth().height(256.dp).padding(horizontal = 12.dp)
      )
    }

    Column(
      modifier = modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      Text(
        text = "Rating",
        modifier = modifier.fillMaxWidth().padding(12.dp),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      Slider(
        modifier = modifier.semantics { contentDescription = "Rating" }.padding(horizontal = 12.dp),
        value = sliderPosition,
        onValueChange = { sliderPosition = it },
        steps = 3,
        valueRange = 1f..5f
      )

      Text(text = sliderPosition.toInt().toString())
    }

    Button(modifier = modifier.size(TextFieldDefaults.MinWidth, 48.dp), onClick = { /*TODO*/}) {
      Text("Add review", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
  }
}
