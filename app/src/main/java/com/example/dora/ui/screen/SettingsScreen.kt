package com.example.dora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dora.R
import com.example.dora.viewmodel.SettingsViewModel

@Composable
internal fun SettingsScreen(settingsViewModel: SettingsViewModel, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val context = LocalContext.current
        val theme by settingsViewModel.theme.collectAsState(initial = "")

        Spacer(modifier = modifier.size(24.dp))

        Row(
            modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Dark theme",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
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
    }
}

@Preview(showBackground = true)
@Composable
internal fun SettingsScreenPreview() {
    // SettingsScreen(modifier = Modifier)
}
