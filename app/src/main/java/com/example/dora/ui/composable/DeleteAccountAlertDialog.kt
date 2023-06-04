package com.example.dora.ui.composable

import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import arrow.core.Either
import com.example.dora.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
internal fun DeleteAccountAlertDialog(
  settingsViewModel: SettingsViewModel,
  onDismiss: () -> Unit,
  onAccountDeleted: () -> Unit,
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  var errorMessage by rememberSaveable { mutableStateOf("") }
  var errorMessageHidden by rememberSaveable { mutableStateOf(true) }

  AlertDialog(
    onDismissRequest = { onDismiss() },
    title = { Text(text = "Delete account") },
    text = { Text(text = if (!errorMessageHidden) errorMessage else "Are you sure about that?") },
    dismissButton = {
      TextButton(onClick = { onDismiss() }) {
        Text(text = "Cancel", color = MaterialTheme.colorScheme.onPrimary)
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          scope.launch {
            when (val result = settingsViewModel.deleteAccount()) {
              is Either.Left -> {
                errorMessage = result.value.message
                errorMessageHidden = false
              }
              is Either.Right -> {
                onAccountDeleted()
                Toast.makeText(context, result.value.message, Toast.LENGTH_SHORT).show()
              }
            }
          }
        }
      ) {
        Text(text = "Delete", color = Color.Red)
      }
    }
  )
}
