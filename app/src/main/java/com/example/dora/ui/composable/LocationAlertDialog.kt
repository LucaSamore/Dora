package com.example.dora.ui.composable

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
internal fun AlertDialogComposable(
  applicationContext: Context,
  showAlertDialog: MutableState<Boolean>
) {
  AlertDialog(
    onDismissRequest = { showAlertDialog.value = false },
    title = { Text(text = "GPS disabled") },
    text = {
      Text(text = "GPS is turned off but is needed if you want to see all the businesses near you")
    },
    confirmButton = {
      TextButton(
        onClick = {
          val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
          if (intent.resolveActivity(applicationContext.packageManager) != null) {
            applicationContext.startActivity(intent)
          }
          showAlertDialog.value = false
        }
      ) {
        Text(text = "Turned on the GPS", color = MaterialTheme.colorScheme.onPrimary)
      }
    },
    dismissButton = {
      TextButton(onClick = { showAlertDialog.value = false }) {
        Text(text = "Dismiss", color = MaterialTheme.colorScheme.onPrimary)
      }
    }
  )
}
