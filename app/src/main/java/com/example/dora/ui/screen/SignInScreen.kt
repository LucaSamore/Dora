package com.example.dora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import arrow.core.Either
import com.example.dora.viewmodel.SignInViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    signInViewModel: SignInViewModel,
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Dora", style = MaterialTheme.typography.titleLarge)

        SignInForm(signInViewModel, onSignIn, modifier)

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Don’t have an account yet?")
            TextButton(onClick = { onSignUp() }) {
                Text(text = "Sign Up")
            }
        }
    }
}

@Composable
fun SignInForm(
    signInViewModel: SignInViewModel,
    onSignIn: () -> Unit,
    modifier: Modifier
) {
    var emailAddress by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 48.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign In", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = modifier.padding(12.dp))

        Text(text = errorMessage, color = Color.Red)

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
                if(passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
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
            onClick = {
                GlobalScope.launch(Dispatchers.Main) {
                    when (val signInResult = signInViewModel.signIn(emailAddress, password)) {
                        is Either.Left -> errorMessage = signInResult.value.message
                        is Either.Right -> onSignIn()
                    }
                }
            }
        ) {
            Text("Sign In")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    // SignInScreen()
}