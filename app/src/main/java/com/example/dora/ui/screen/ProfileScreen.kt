package com.example.dora.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import arrow.core.Either
import arrow.core.getOrElse
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.dora.common.validation.UserValidator
import com.example.dora.common.validation.ValidationStatus
import com.example.dora.model.*
import com.example.dora.ui.composable.ErrorAlertDialog
import com.example.dora.ui.composable.ProfilePicture
import com.example.dora.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
internal fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    paddingValues: PaddingValues,
    modifier: Modifier,
    onError: () -> Unit,
    onUpdate: () -> Unit
) {
    Column(
        modifier =
            modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(paddingValues),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val eitherUser by profileViewModel.user.collectAsState()

        if (eitherUser.isLeft()) {
            ErrorAlertDialog(
                title = "Error",
                content = "Unable to retrieve your data",
                onError = onError
            )
        } else {
            val user = eitherUser.getOrNull()
            // TODO: Fix this
            if (user?.uid != null) {
                ProfileForm(
                    profileViewModel = profileViewModel,
                    modifier = modifier,
                    user = user,
                    onUpdate = onUpdate
                )
            }
        }
    }
}

@Composable
internal fun ProfileForm(
    profileViewModel: ProfileViewModel,
    modifier: Modifier,
    user: User,
    onUpdate: () -> Unit
) {
    var firstName by rememberSaveable { mutableStateOf(user.firstName ?: "") }
    var lastName by rememberSaveable { mutableStateOf(user.lastName ?: "") }
    var emailAddress by rememberSaveable { mutableStateOf(user.emailAddress ?: "") }
    var passwordConfirmation by rememberSaveable { mutableStateOf("") }
    var passwordConfirmationHidden by rememberSaveable { mutableStateOf(true) }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var errorMessageHidden by rememberSaveable { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var imageUri by remember {
        mutableStateOf<Uri>(
            if (user.profilePicture != null) Uri.parse(user.profilePicture) else Uri.EMPTY
        )
    }
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            imageUri = it ?: Uri.EMPTY
        }

    if (imageUri != Uri.EMPTY) {
        ProfilePicture(image = imageUri, context = context, defaultAvatar = false)
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
            Text(text = "Gallery")
        }

        TextButton(onClick = { imageUri = Uri.EMPTY }) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete profile picture")
            Spacer(modifier = modifier.size(2.dp))
            Text(text = "Remove")
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
        value = passwordConfirmation,
        onValueChange = { passwordConfirmation = it },
        singleLine = true,
        label = { Text("Password confirmation") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation =
            if (passwordConfirmationHidden) PasswordVisualTransformation()
            else VisualTransformation.None,
        trailingIcon = {
            IconButton(onClick = { passwordConfirmationHidden = !passwordConfirmationHidden }) {
                val visibilityIcon =
                    if (!passwordConfirmationHidden) Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                val description =
                    if (passwordConfirmationHidden) "Show password" else "Hide password"
                Icon(imageVector = visibilityIcon, contentDescription = description)
            }
        }
    )

    Button(
        modifier = modifier.size(TextFieldDefaults.MinWidth, 48.dp),
        onClick = {
            if (passwordConfirmation.isEmpty()) {
                errorMessage = "Password required"
                errorMessageHidden = false
                return@Button
            }

            val passwordVerificationResult =
                BCrypt.verifyer()
                    .verify(passwordConfirmation.toByteArray(), user.password?.toByteArray())

            if (!passwordVerificationResult.verified) {
                errorMessage = "Password is not correct"
                errorMessageHidden = false
                return@Button
            }

            UserValidator.validateFirstOrLastName(firstName).also {
                if (it.status == ValidationStatus.REJECT) {
                    errorMessage = it.message!!
                    errorMessageHidden = false
                    return@Button
                }
            }

            UserValidator.validateFirstOrLastName(lastName).also {
                if (it.status == ValidationStatus.REJECT) {
                    errorMessage = it.message!!
                    errorMessageHidden = false
                    return@Button
                }
            }

            UserValidator.validateEmailAddress(emailAddress).also {
                if (it.status == ValidationStatus.REJECT) {
                    errorMessage = it.message!!
                    errorMessageHidden = false
                    return@Button
                }
            }

            scope.launch {
                var uri = imageUri

                if (imageUri.toString() != user.profilePicture) {
                    uri =
                        profileViewModel.updateProfilePicture(user.uid!!, imageUri).getOrElse {
                            Uri.EMPTY
                        }
                }

                val changes =
                    opticsCompose(
                        user,
                        { u -> User.firstName.set(u, firstName) },
                        { u -> User.lastName.set(u, lastName) },
                        { u -> User.emailAddress.set(u, emailAddress) },
                        { u -> User.profilePicture.set(u, uri.toString()) },
                    )

                when (val result = profileViewModel.updateProfile(changes)) {
                    is Either.Left -> {
                        errorMessage = result.value.message
                        errorMessageHidden = false
                    }
                    is Either.Right -> {
                        onUpdate()
                    }
                }
            }
        }
    ) {
        Text("Update profile")
    }
}

@Preview(showBackground = true)
@Composable
internal fun ProfileScreenPreview() {
    // ProfileScreen(modifier = Modifier)
}
