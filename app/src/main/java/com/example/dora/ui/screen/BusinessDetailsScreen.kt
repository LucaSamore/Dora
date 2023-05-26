package com.example.dora.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.dora.model.Business
import com.example.dora.viewmodel.BusinessDetailsViewModel
import kotlinx.coroutines.launch

@Composable
internal fun BusinessDetailsScreen(
    businessDetailsViewModel: BusinessDetailsViewModel,
    businessId: String,
    modifier: Modifier
) {
    val scope = rememberCoroutineScope()
    val business = remember { mutableStateOf<Business?>(null) }
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
                    { right -> business.value = right }
                )
        }
    }

    if (business.value == null) return

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = business.value?.name!!)
    }
}
