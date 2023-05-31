package com.example.dora.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dora.R
import com.example.dora.ui.composable.DeleteAccountAlertDialog
import com.example.dora.viewmodel.SettingsViewModel

@Composable
internal fun SettingsScreen(
  settingsViewModel: SettingsViewModel,
  paddingValues: PaddingValues,
  modifier: Modifier,
  onSignOut: () -> Unit,
  onDismiss: () -> Unit,
  onAccountDeleted: () -> Unit,
  onMyBusinessesClicked: () -> Unit,
) {
  Column(
    modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(paddingValues),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    val context = LocalContext.current
    val theme by settingsViewModel.theme.collectAsState(initial = "")
    var showDeleteAccountDialog by rememberSaveable { mutableStateOf(false) }

    Row(
      modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = "Dark theme",
        style = MaterialTheme.typography.bodyLarge,
      )
      Switch(
        modifier = Modifier.semantics { contentDescription = "Dark mode" },
        checked = theme == context.getString(R.string.dark_theme),
        onCheckedChange = {
          if (theme == context.getString(R.string.dark_theme)) {
            settingsViewModel.saveTheme(context.getString(R.string.light_theme))
          } else {
            settingsViewModel.saveTheme(context.getString(R.string.dark_theme))
          }
        }
      )
    }

    Divider()

    ListItem(
      modifier = modifier.clickable { onMyBusinessesClicked() },
      headlineContent = {
        Text(
          text = "My businesses",
          style = MaterialTheme.typography.bodyLarge,
        )
      },
      trailingContent = { Icon(Icons.Filled.ArrowForward, contentDescription = "My businesses") }
    )

    Divider()

    ListItem(
      modifier = modifier.clickable { showDeleteAccountDialog = true },
      headlineContent = {
        Text(
          text = "Delete my account",
          style = MaterialTheme.typography.bodyLarge,
        )
      },
      trailingContent = { Icon(Icons.Filled.ArrowForward, contentDescription = "Delete account") }
    )

    Divider()

    ListItem(
      modifier =
        modifier.clickable {
          settingsViewModel.signOut()
          onSignOut()
        },
      headlineContent = {
        Text(
          text = "Sign out",
          style = MaterialTheme.typography.bodyLarge,
        )
      },
      trailingContent = { Icon(Icons.Filled.ArrowForward, contentDescription = "Sign out") }
    )

    Divider()

    if (showDeleteAccountDialog) {
      DeleteAccountAlertDialog(
        settingsViewModel = settingsViewModel,
        onDismiss = onDismiss,
        onAccountDeleted = onAccountDeleted,
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
internal fun SettingsScreenPreview() {
  // SettingsScreen(modifier = Modifier)
}
