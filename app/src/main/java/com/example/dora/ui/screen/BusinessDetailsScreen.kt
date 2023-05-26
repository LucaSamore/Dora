package com.example.dora.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dora.model.Business
import com.example.dora.viewmodel.BusinessDetailsViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun BusinessDetailsScreen(
    businessDetailsViewModel: BusinessDetailsViewModel,
    businessId: String,
    modifier: Modifier,
    paddingValues: PaddingValues,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    var business by remember { mutableStateOf<Business?>(null) }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var errorMessageHidden by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        scope.launch {
            businessDetailsViewModel
                .getBusiness(businessId)
                .fold(
                    { left ->
                        errorMessage = left.message
                        errorMessageHidden = false
                    },
                    { right -> business = right }
                )
        }
    }

    if (business == null) return

    val cameraPositionState = rememberCameraPositionState {
        position =
            CameraPosition.fromLatLngZoom(
                LatLng(
                    business?.address?.location?.latitude!!,
                    business?.address?.location?.longitude!!
                ),
                10f
            )
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState(), enabled = !cameraPositionState.isMoving),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!errorMessageHidden) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = modifier.padding(top = 4.dp, bottom = 6.dp),
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = business?.name!!,
            modifier = modifier.fillMaxWidth().padding(12.dp),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left
        )

        Text(
            text = business?.description!!,
            modifier = modifier.fillMaxWidth().padding(12.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Left
        )

        Text(
            text = "Gallery",
            modifier = modifier.fillMaxWidth().padding(12.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left
        )

        LazyRow {
            items(business?.images!!) { image ->
                AsyncImage(
                    model =
                        ImageRequest.Builder(context)
                            .data(image)
                            .size(1280, 720)
                            .crossfade(true)
                            .build(),
                    contentDescription = "Business photo",
                )
            }
        }

        Text(
            text = "Owner",
            modifier = modifier.fillMaxWidth().padding(12.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left
        )

        Text(
            text = "${business?.owner?.firstName!!} ${business?.owner?.lastName!!}",
            modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 2.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Left
        )

        Text(
            text = "Contacts: ${business?.owner?.emailAddress!!}",
            modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 2.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Left,
        )

        GoogleMap(
            modifier = Modifier.fillMaxSize().height(256.dp).padding(12.dp),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                position =
                    LatLng(
                        business?.address?.location?.latitude!!,
                        business?.address?.location?.longitude!!
                    ),
                title = business?.address?.name,
                snippet = "Marker in ${business?.address?.address}"
            )
        }

        Text(
            text = "Informations",
            modifier = modifier.fillMaxWidth().padding(12.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left
        )

        Text(
            text = "Address: ${business?.address?.address!!}",
            modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 2.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Left,
        )

        Text(
            text = "Phone number: ${business?.phoneNumber!!}",
            modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 2.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Left,
        )

        if (!business?.website.isNullOrEmpty()) {
            Text(
                text = "Website: ${business?.website!!}",
                modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 2.dp),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Left,
            )
        }

        Text(
            text = if (business?.isOpen!!) "Open" else "Closed",
            modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 2.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold
        )
    }
}
