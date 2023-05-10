package com.example.dora.ui.composable

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.material3.*
import android.provider.Settings

@Composable
internal fun AlertDialogComposable(
    applicationContext: Context,
    showAlertDialog: MutableState<Boolean>
) {
    AlertDialog(
        onDismissRequest = {
            showAlertDialog.value = false
        },
        title = {
            Text(text = "GPS disabled")
        },
        text = {
            Text(text = "GPS is turned off but is needed to get the coordinates")
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
                Text("Turned on the GPS")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { showAlertDialog.value = false  }
            ) {
                Text("Dismiss")
            }
        }
    )
}
