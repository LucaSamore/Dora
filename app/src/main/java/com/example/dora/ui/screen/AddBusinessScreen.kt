package com.example.dora.ui.screen

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dora.BuildConfig
import com.example.dora.common.BusinessPlace
import com.example.dora.common.Location
import com.example.dora.model.Category
import com.example.dora.viewmodel.AddBusinessViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun AddBusinessScreen(
    addBusinessViewModel: AddBusinessViewModel,
    paddingValues: PaddingValues,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState()
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var website by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var businessPlace by rememberSaveable { mutableStateOf<BusinessPlace?>(null) }
    val categories = Category.values()
    var expanded by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf(categories[0]) }
    val images = remember { mutableStateListOf<Uri>(Uri.EMPTY) }
    var isOpen by rememberSaveable { mutableStateOf(false) }
    var showBusinessPlaceName by rememberSaveable { mutableStateOf(false) }
    var showCarousel by rememberSaveable { mutableStateOf(false) }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            images.apply {
                clear()
                addAll(it)
            }
            showCarousel = true
        }
    val intentLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val place = Autocomplete.getPlaceFromIntent(intent)
                    businessPlace =
                        BusinessPlace(
                            place.id!!,
                            place.name!!,
                            place.address!!,
                            Location(place.latLng?.latitude!!, place.latLng?.longitude!!)
                        )
                    showBusinessPlaceName = true
                }
            }
        }
    val launchMapInputOverlay = {
        Places.initialize(context, BuildConfig.MAPS_API_KEY)
        val fields =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
        val intent =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(context)
        intentLauncher.launch(intent)
    }

    Column(
        modifier =
            modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(paddingValues),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showCarousel) {
            HorizontalPager(pageCount = images.size, state = pagerState) { pageNumber ->
                images.forEachIndexed { index, uri ->
                    if (pageNumber == index) {
                        AsyncImage(
                            model =
                                ImageRequest.Builder(context)
                                    .data(uri)
                                    .size(320, 320)
                                    .crossfade(true)
                                    .build(),
                            contentDescription = "Business photo ${pageNumber + 1}",
                        )
                        return@forEachIndexed
                    }
                }
            }
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                )
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (optional)") },
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                )
        )

        OutlinedTextField(
            value = website,
            onValueChange = { website = it },
            label = { Text("Website (optional)") },
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                )
        )

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone number") },
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                )
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                modifier = modifier.menuAnchor(),
                readOnly = true,
                value = category.categoryName,
                onValueChange = {},
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors =
                    ExposedDropdownMenuDefaults.textFieldColors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    ),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                categories.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption.categoryName) },
                        onClick = {
                            category = selectionOption
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Is open",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            Switch(
                modifier = Modifier.semantics { contentDescription = "Is open" },
                checked = isOpen,
                onCheckedChange = { isOpen = !isOpen }
            )
        }

        if (showBusinessPlaceName) {
            Text(text = businessPlace?.name!!)
        }

        Button(
            modifier = modifier.size(TextFieldDefaults.MinWidth, 48.dp),
            onClick = launchMapInputOverlay
        ) {
            Icon(Icons.Filled.Search, "Search place")
            Spacer(modifier = modifier.size(6.dp))
            Text(text = "Add business address")
        }

        Button(
            modifier = modifier.size(TextFieldDefaults.MinWidth, 48.dp),
            onClick = { galleryLauncher.launch("image/*") }
        ) {
            Icon(Icons.Filled.Photo, "Browse gallery")
            Spacer(modifier = modifier.size(6.dp))
            Text(text = "Add business images")
        }

        Button(modifier = modifier.size(TextFieldDefaults.MinWidth, 48.dp), onClick = {}) {
            Text(
                text = "Create",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

/**
 * name [X] description [X] address (street name, number, city, country) [X] website [X] phoneNumber
 * [X] category [X] isOpen [] images [X]
 */
