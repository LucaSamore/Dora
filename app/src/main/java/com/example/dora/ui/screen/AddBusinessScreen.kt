package com.example.dora.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AddBusinessScreen(
    paddingValues: PaddingValues,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState()
    val images = remember { mutableStateListOf<Uri>(Uri.EMPTY) }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) {
            images.apply {
                clear()
                addAll(it)
            }
        }

    Column(
        modifier =
            modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(paddingValues),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(pageCount = images.size, state = pagerState) { pageNumber ->
            images.forEachIndexed { index, uri ->
                if (pageNumber == index) {
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(uri).crossfade(true).build(),
                        contentDescription = "Business photo ${pageNumber + 1}",
                    )
                    return@forEachIndexed
                }
            }
        }

        Button(onClick = { galleryLauncher.launch("image/*") }) { Text(text = "Get images") }
    }
}
