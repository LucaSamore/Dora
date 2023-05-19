package com.example.dora.ui.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
internal fun ErrorAlertDialog(title: String, content: String, onError: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onError() },
        title = { Text(text = title) },
        text = { Text(text = content) },
        confirmButton = { TextButton(onClick = { onError() }) { Text("Got it") } }
    )
}
