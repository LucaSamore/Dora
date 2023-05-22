package com.example.dora.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AddBusinessScreen(
    paddingValues: PaddingValues,
    modifier: Modifier,
) {
    val pagerState = rememberPagerState()
    
    Column(
        modifier =
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add business screen", style = MaterialTheme.typography.titleLarge)
        
        HorizontalPager(pageCount = 2, state = pagerState) { page ->
            if (page == 0) {
                Card(
                    modifier = modifier.size(256.dp, 128.dp)
                ) {
                    Text(text = "Ciao")
                    Text(text = "Bella")
                }
            } else if (page == 1) {
                Card(
                    modifier = modifier.size(256.dp, 128.dp)
                ) {
                    Text(text = "Hey")
                    Text(text = "You")
                }
            }
        }
    }
}
