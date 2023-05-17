package com.example.dora.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import arrow.core.left
import com.example.dora.common.ErrorMessage
import com.example.dora.ui.composable.ProfilePicture
import com.example.dora.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var firstName by rememberSaveable { mutableStateOf("") }
        var lastName by rememberSaveable { mutableStateOf("") }
        var emailAddress by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var passwordHidden by rememberSaveable { mutableStateOf(true) }
        var errorMessage by rememberSaveable { mutableStateOf("this is an error") }
        var errorMessageHidden by rememberSaveable { mutableStateOf(true) }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        var imageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
        val galleryLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
                imageUri = it ?: Uri.EMPTY
            }

        val user by profileViewModel.user.collectAsState(initial = ErrorMessage("").left())

        firstName = user.fold({ e -> e.message }, { u -> u.firstName })

        ProfilePicture(context = context)

        Spacer(modifier = modifier.padding(6.dp))

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            TextButton(onClick = { galleryLauncher.launch("image/*") }) {
                Text(text = "Get from gallery")
            }
        }

        if (!errorMessageHidden) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = modifier.padding(top = 4.dp, bottom = 6.dp)
            )
        }

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First name") },
            placeholder = { Text("Mario") },
        )

        Spacer(modifier = modifier.padding(6.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last name") },
            placeholder = { Text("Rossi") },
        )

        Spacer(modifier = modifier.padding(6.dp))

        OutlinedTextField(
            value = emailAddress,
            onValueChange = { emailAddress = it },
            label = { Text("Email address") },
            placeholder = { Text("example@gmail.com") },
        )

        Spacer(modifier = modifier.padding(6.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation =
                if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                    val visibilityIcon =
                        if (!passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    // Please provide localized description for accessibility services
                    val description = if (passwordHidden) "Show password" else "Hide password"
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                }
            }
        )

        Spacer(modifier = modifier.padding(16.dp))

        Button(
            modifier = modifier.size(TextFieldDefaults.MinWidth, 48.dp),
            onClick = { errorMessageHidden = !errorMessageHidden }
        ) {
            Text("Update profile")
        }

        Spacer(modifier = modifier.padding(48.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    // ProfileScreen(modifier = Modifier)
}
