package com.example.dora.ui.screen

import android.app.Activity
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dora.BuildConfig
import com.example.dora.common.BusinessPlace
import com.example.dora.common.Location
import com.example.dora.common.validation.BusinessValidator
import com.example.dora.common.validation.Validator
import com.example.dora.model.Category
import com.example.dora.viewmodel.AddBusinessViewModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun AddBusinessScreen(
  addBusinessViewModel: AddBusinessViewModel,
  paddingValues: PaddingValues,
  modifier: Modifier,
  onCreated: () -> Unit,
) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val pagerState = rememberPagerState()
  var spacing by rememberSaveable { mutableStateOf(0) }
  var name by rememberSaveable { mutableStateOf("") }
  var description by rememberSaveable { mutableStateOf("") }
  var website by rememberSaveable { mutableStateOf("") }
  var phoneNumber by rememberSaveable { mutableStateOf("") }
  var address by rememberSaveable { mutableStateOf<BusinessPlace?>(null) }
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
      spacing = 12
    }
  val intentLauncher =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      result: ActivityResult ->
      if (result.resultCode == Activity.RESULT_OK) {
        val intent = result.data
        if (intent != null) {
          val place = Autocomplete.getPlaceFromIntent(intent)
          address =
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
    val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
    val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(context)
    intentLauncher.launch(intent)
  }
  var errorMessage by rememberSaveable { mutableStateOf("") }
  var errorMessageHidden by rememberSaveable { mutableStateOf(true) }
  var progressIndicatorHidden by rememberSaveable { mutableStateOf(true) }

  Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Column(
      modifier =
        modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(paddingValues),
      verticalArrangement = Arrangement.SpaceEvenly,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      if (showCarousel) {
        Spacer(modifier = modifier.size(spacing.dp))
        HorizontalPager(pageCount = images.size, state = pagerState) { pageNumber ->
          images.forEachIndexed { index, uri ->
            if (pageNumber == index) {
              AsyncImage(
                model =
                  ImageRequest.Builder(context).data(uri).size(1280, 720).crossfade(true).build(),
                contentDescription = "Business photo ${pageNumber + 1}",
                modifier = modifier.clip(RoundedCornerShape(10))
              )
              return@forEachIndexed
            }
          }
        }
        Spacer(modifier = modifier.size(spacing.dp))
      }

      if (!errorMessageHidden) {
        Text(
          text = errorMessage,
          color = Color.Red,
          modifier = modifier.padding(top = 4.dp, bottom = 6.dp),
          textAlign = TextAlign.Center
        )
      }

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

      Spacer(modifier = modifier.size(spacing.dp))

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

      Spacer(modifier = modifier.size(spacing.dp))

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

      Spacer(modifier = modifier.size(spacing.dp))

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

      Spacer(modifier = modifier.size(spacing.dp))

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

      Spacer(modifier = modifier.size(spacing.dp))

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

      Spacer(modifier = modifier.size(spacing.dp))

      if (showBusinessPlaceName) {
        Text(text = address?.name!!)
        Spacer(modifier = modifier.size(spacing.dp))
      }

      Button(
        modifier = modifier.size(TextFieldDefaults.MinWidth, 48.dp),
        onClick = launchMapInputOverlay
      ) {
        Icon(Icons.Filled.Search, "Search place")
        Spacer(modifier = modifier.size(6.dp))
        Text(text = "Add business address")
      }

      Spacer(modifier = modifier.size(spacing.dp))

      Button(
        modifier = modifier.size(TextFieldDefaults.MinWidth, 48.dp),
        onClick = { galleryLauncher.launch("image/*") }
      ) {
        Icon(Icons.Filled.Photo, "Browse gallery")
        Spacer(modifier = modifier.size(6.dp))
        Text(text = "Add business images")
      }

      Spacer(modifier = modifier.size(spacing.dp))

      Button(
        modifier = modifier.size(TextFieldDefaults.MinWidth, 48.dp),
        onClick = {
          // Unfortunately validator pipelines work only on a single type at the time

          Validator.pipeline(
              Validator.Pipe(name, BusinessValidator::validateName),
              Validator.Pipe(website, BusinessValidator::validateWebsite),
              Validator.Pipe(phoneNumber, BusinessValidator::validatePhoneNumber),
            )
            .ifRejected {
              errorMessage = it.message!!
              errorMessageHidden = false
              return@Button
            }

          Validator.pipeline(Validator.Pipe(address, BusinessValidator::validateAddress))
            .ifRejected {
              errorMessage = it.message!!
              errorMessageHidden = false
              return@Button
            }

          Validator.pipeline(
              Validator.Pipe(images.toList(), BusinessValidator::validateImages),
            )
            .ifRejected {
              errorMessage = it.message!!
              errorMessageHidden = false
              return@Button
            }

          progressIndicatorHidden = false

          scope.launch {
            addBusinessViewModel
              .createBusiness(
                name = name,
                description = description,
                address = address,
                website = website,
                phoneNumber = phoneNumber,
                category = category,
                isOpen = isOpen,
                images = images.filter { uri -> uri != Uri.EMPTY }.toList()
              )
              .fold(
                { left ->
                  progressIndicatorHidden = true
                  errorMessage = left.message
                  errorMessageHidden = false
                },
                { right ->
                  onCreated()
                  progressIndicatorHidden = true
                  Toast.makeText(context, right.message, Toast.LENGTH_SHORT).show()
                }
              )
          }
        }
      ) {
        Text(
          text = "Create",
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onPrimary
        )
      }

      Spacer(modifier = modifier.size(spacing.dp))
    }

    if (!progressIndicatorHidden) {
      CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
    }
  }
}
