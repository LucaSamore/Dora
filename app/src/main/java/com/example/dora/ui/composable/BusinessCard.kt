package com.example.dora.ui.composable

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dora.model.Business
import com.example.dora.ui.navigation.DoraScreen

@Composable
internal fun BusinessCard(
  business: Business,
  context: Context,
  modifier: Modifier,
  navController: NavHostController
) {
  Card(
    modifier =
      modifier.fillMaxWidth().padding(12.dp).height(256.dp).clickable {
        navController.navigate("${DoraScreen.BusinessDetails.name}/${business.uuid}")
      },
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
  ) {
    Row(
      modifier = modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box {
        if (business.images?.isNotEmpty()!!) {
          AsyncImage(
            model =
              ImageRequest.Builder(context)
                .data(Uri.parse(business.images?.first()))
                .crossfade(true)
                .build(),
            contentDescription = "Image of ${business.name}",
          )
        }
      }

      Box {
        Column(
          verticalArrangement = Arrangement.Top,
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = modifier.padding(6.dp).fillMaxHeight()
        ) {
          Text(
            text = business.name!!,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(6.dp)
          )

          Text(
            text = business.description!!,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(6.dp)
          )

          Text(
            text = "Phone number: ${business.phoneNumber!!}",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(6.dp)
          )

          Text(
            text = if (business.isOpen!!) "Open" else "Closed",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(6.dp),
          )
        }
      }
    }
  }
}
