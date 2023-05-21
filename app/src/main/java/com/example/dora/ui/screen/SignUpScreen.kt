package com.example.dora.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import arrow.core.Either
import com.example.dora.ui.composable.ProfilePicture
import com.example.dora.ui.util.createImageFile
import com.example.dora.viewmodel.SignUpViewModel
import java.util.*
import kotlinx.coroutines.launch

@Composable
internal fun SignUpScreen(
    signUpViewModel: SignUpViewModel,
    paddingValues: PaddingValues,
    modifier: Modifier,
    onSignUp: () -> Unit,
    onBackToSignIn: () -> Unit
) {
    Column(
        modifier =
            modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(paddingValues),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SignUpForm(signUpViewModel, onSignUp, modifier)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Already have an account?")
            TextButton(onClick = { onBackToSignIn() }) { Text(text = "Sign In") }
        }
    }
}

@Composable
internal fun SignUpForm(
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
    var errorMessageHidden by rememberSaveable { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri =
        FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            context.packageName + ".provider",
            file
        )
    var imageUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { imageUri = uri }
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            imageUri = it ?: Uri.EMPTY
        }
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Permission was denied", Toast.LENGTH_SHORT).show()
            }
        }

    if (imageUri.path?.isNotEmpty() == true) {
        ProfilePicture(image = imageUri, context = context, defaultAvatar = false)
        // saveImage(context.applicationContext.contentResolver, imageUri)
    } else {
        ProfilePicture(context = context)
    }

    Row(
        modifier = modifier.width(TextFieldDefaults.MinWidth),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        TextButton(onClick = { galleryLauncher.launch("image/*") }) {
            Icon(Icons.Filled.Image, contentDescription = "Get from gallery")
            Spacer(modifier = modifier.size(2.dp))
            Text(text = "Get from gallery")
        }

        TextButton(
            onClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
        ) {
            Icon(Icons.Filled.PhotoCamera, contentDescription = "Take picture")
            Spacer(modifier = modifier.size(2.dp))
            Text(text = "Take picture")
        }
    }

    if (!errorMessageHidden) {
        Text(
            text = errorMessage,
            color = Color.Red,
            modifier = modifier.padding(top = 4.dp, bottom = 6.dp),
            textAlign = TextAlign.Center
        )
    }

    OutlinedTextField(
        value = firstName,
        onValueChange = { firstName = it },
        label = { Text("First name") },
        placeholder = { Text("Mario") },
    )

    OutlinedTextField(
        value = lastName,
        onValueChange = { lastName = it },
        label = { Text("Last name") },
        placeholder = { Text("Rossi") },
    )

    OutlinedTextField(
        value = emailAddress,
        onValueChange = { emailAddress = it },
        label = { Text("Email address") },
        placeholder = { Text("example@gmail.com") },
    )

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
                val description = if (passwordHidden) "Show password" else "Hide password"
                Icon(imageVector = visibilityIcon, contentDescription = description)
            }
        }
    )

    Button(
        modifier = modifier.size(TextFieldDefaults.MinWidth, 48.dp),
        onClick = {
            scope.launch {
                when (
                    val signUpResult =
                        signUpViewModel.signUp(
                            firstName,
                            lastName,
                            emailAddress,
                            password,
                            imageUri
                        )
                ) {
                    is Either.Left -> {
                        errorMessageHidden = false
                        errorMessage = signUpResult.value.message
                    }
                    is Either.Right -> onSignUp()
                }
            }
        }
    ) {
        Text("Sign Up")
    }
}

@Preview(showBackground = true)
@Composable
internal fun SignUpScreenPreview() {
    // SignUpScreen()
}
