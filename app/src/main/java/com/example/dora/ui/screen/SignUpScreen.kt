package com.example.dora.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import arrow.core.Either
import com.example.dora.viewmodel.SignUpViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel,
    onSignUp: () -> Unit,
    onBackToSignIn: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Dora", style = MaterialTheme.typography.titleLarge)

        SignUpForm(signUpViewModel, onSignUp, modifier)

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Already have an account?")
            TextButton(onClick = { onBackToSignIn() }) {
                Text(text = "Sign In")
            }
        }
    }
}

@Composable
fun SignUpForm(
    signUpViewModel: SignUpViewModel,
    onSignUp: () -> Unit,
    modifier: Modifier
) {
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
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
        Text(text = "Sign Up", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = modifier.padding(12.dp))

        Text(text = errorMessage, color = Color.Red)

        Spacer(modifier = modifier.padding(6.dp))

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
                    when (val signUpResult = signUpViewModel.signUp(firstName, lastName, emailAddress, password)) {
                        is Either.Left -> errorMessage = signUpResult.value.message
                        is Either.Right -> onSignUp()
                    }
                }
            }
        ) {
            Text("Sign Up")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    // SignUpScreen()
}