package com.example.dora.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.dora.common.Location
import com.example.dora.model.Business
import com.example.dora.model.Category
import com.example.dora.ui.composable.BusinessCard
import com.example.dora.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    homeViewModel: HomeViewModel,
    modifier: Modifier,
    paddingValues: PaddingValues,
    location: MutableState<Location>,
    startLocationUpdates: () -> Unit,
    navController: NavHostController,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var businesses = remember { mutableStateListOf<Business>() }
    var searchContent by rememberSaveable { mutableStateOf("") }
    var searchBarActive by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var errorMessageHidden by rememberSaveable { mutableStateOf(true) }

    startLocationUpdates()

    homeViewModel.updateLocation(location.value)

    Column(
        modifier = modifier.fillMaxSize().padding(paddingValues),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = modifier.semantics { isContainer = true }.zIndex(1f).fillMaxWidth()) {
            SearchBar(
                modifier = modifier.fillMaxWidth().padding(12.dp),
                query = searchContent,
                onQueryChange = { searchContent = it },
                onSearch = { searchBarActive = false },
                active = searchBarActive,
                onActiveChange = { searchBarActive = it },
                placeholder = { Text(text = "Search") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search business") },
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(4) { idx ->
                        val resultText = "Suggestion $idx"
                        ListItem(
                            headlineContent = { Text(resultText) },
                            supportingContent = { Text("Additional info") },
                            leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                            modifier =
                                modifier.clickable {
                                    searchContent = resultText
                                    searchBarActive = false
                                }
                        )
                    }
                }
            }
        }

        Column(
            modifier = modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondary),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(12.dp)
            )

            Spacer(modifier = modifier.size(6.dp))

            LazyRow(modifier = modifier.padding(bottom = 12.dp)) {
                items(Category.values()) { category ->
                    FilterChip(
                        label = { Text(text = category.categoryName) },
                        selected = false,
                        enabled = true,
                        onClick = {},
                        colors =
                            FilterChipDefaults.filterChipColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                selectedContainerColor = MaterialTheme.colorScheme.primary
                            ),
                        modifier = modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }

        Spacer(modifier = modifier.size(12.dp))

        if (!errorMessageHidden) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = modifier.padding(top = 4.dp, bottom = 6.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = modifier.size(12.dp))

        LazyColumn {
            items(businesses) { business ->
                BusinessCard(business, context, modifier, navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun HomeScreenPreview() {
    // HomeScreen()
}
