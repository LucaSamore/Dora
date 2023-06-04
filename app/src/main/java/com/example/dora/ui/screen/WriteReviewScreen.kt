package com.example.dora.ui.screen

import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.dora.common.validation.ReviewValidator
import com.example.dora.common.validation.Validator
import com.example.dora.viewmodel.WriteReviewViewModel
import kotlinx.coroutines.launch

@Composable
internal fun WriteReviewScreen(
  writeReviewViewModel: WriteReviewViewModel,
  businessId: String,
  modifier: Modifier,
  paddingValues: PaddingValues,
  onSuccess: () -> Unit,
) {
  val scope = rememberCoroutineScope()
  val context = LocalContext.current
  var content by rememberSaveable { mutableStateOf("") }
  var rating by remember { mutableStateOf(1f) }
  var errorMessage by rememberSaveable { mutableStateOf("") }
  var errorMessageHidden by rememberSaveable { mutableStateOf(true) }

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
        value = rating,
        onValueChange = { rating = it },
        steps = 3,
        valueRange = 1f..5f
      )

      Text(text = rating.toInt().toString())
    }

    if (!errorMessageHidden) {
      Text(
        text = errorMessage,
        color = Color.Red,
        modifier = modifier.padding(top = 4.dp, bottom = 6.dp),
        textAlign = TextAlign.Center
      )
    }

    Button(
      modifier = modifier.size(TextFieldDefaults.MinWidth, 48.dp),
      onClick = {
        Validator.pipeline(
            Validator.Pipe(content, ReviewValidator::validateContent),
          )
          .ifRejected {
            errorMessage = it.message!!
            errorMessageHidden = false
            return@Button
          }

        Validator.pipeline(
            Validator.Pipe(rating.toInt(), ReviewValidator::validateRating),
          )
          .ifRejected {
            errorMessage = it.message!!
            errorMessageHidden = false
            return@Button
          }

        scope.launch {
          writeReviewViewModel
            .addReview(
              businessId = businessId,
              content = content,
              rating = rating.toInt(),
            )
            .fold(
              { left ->
                errorMessage = left.message
                errorMessageHidden = false
              },
              { right ->
                onSuccess()
                Toast.makeText(context, right.message, Toast.LENGTH_SHORT).show()
              }
            )
        }
      }
    ) {
      Text("Add review", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
  }
}
