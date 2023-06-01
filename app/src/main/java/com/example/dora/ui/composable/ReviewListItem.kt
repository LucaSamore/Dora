package com.example.dora.ui.composable

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dora.model.Review
import com.example.dora.viewmodel.BusinessDetailsViewModel

@Composable
internal fun ReviewListItem(
  businessDetailsViewModel: BusinessDetailsViewModel,
  review: Review,
  modifier: Modifier
) {
  val scope = rememberCoroutineScope()
  val context = LocalContext.current
  var votes by remember { mutableStateOf(0) }
  var filledHeart by remember { mutableStateOf(false) }
  var errorMessage by rememberSaveable { mutableStateOf("") }
  var errorMessageHidden by rememberSaveable { mutableStateOf(true) }

  Column(
    modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
  ) {
    LazyRow(
      modifier = modifier.fillMaxWidth().padding(vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      item {
        Row(
          verticalAlignment = Alignment.Top,
        ) {
          ProfilePicture(
            image = Uri.parse(review.user?.profilePicture),
            context = context,
            size = Pair(64.dp, 64.dp),
            defaultAvatar = false,
          )

          Column(
            modifier = modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
          ) {
            Text(
              text = "${review.user?.firstName} ${review.user?.lastName}",
              style = MaterialTheme.typography.bodyLarge,
              fontWeight = FontWeight.Bold,
              modifier = modifier.padding(vertical = 4.dp),
            )
            Row {
              repeat(review.rating!!) {
                Icon(Icons.Filled.Star, contentDescription = "Rating star")
              }
            }
          }
        }
      }
      item {
        Column(
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          IconToggleButton(
            checked = filledHeart,
            onCheckedChange = {
              filledHeart = !filledHeart
              // TODO
            }
          ) {
            Icon(
              if (filledHeart) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
              contentDescription = "Toggle like review",
              tint = MaterialTheme.colorScheme.onPrimary
            )
          }
          Text(
            text = votes.toString(),
            modifier = modifier.padding(horizontal = 6.dp),
            style = MaterialTheme.typography.bodyLarge
          )
        }
      }
    }
    Text(text = review.content!!)
  }
}
