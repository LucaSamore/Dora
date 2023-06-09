package com.example.dora.ui.composable

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState

@Composable
internal fun SnackBarComposable(
  snackbarHostState: SnackbarHostState,
  applicationContext: Context,
  showSnackBar: MutableState<Boolean>
) {
  LaunchedEffect(snackbarHostState) {
    val result =
      snackbarHostState.showSnackbar(
        message = "Permission are needed if you want to see businesses near you",
        actionLabel = "Go to settings",
        duration = SnackbarDuration.Short,
      )
    when (result) {
      SnackbarResult.ActionPerformed -> {
        val intent =
          Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", applicationContext.packageName, null)
          }
        if (intent.resolveActivity(applicationContext.packageManager) != null) {
          applicationContext.startActivity(intent)
        }
      }
      SnackbarResult.Dismissed -> {
        showSnackBar.value = false
      }
    }
  }
}
