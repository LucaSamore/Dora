package com.example.dora.ui.composable

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dora.model.Review

@Composable
internal fun ReviewListItem(review: Review, modifier: Modifier) {
  val context = LocalContext.current
  var votes by remember { mutableStateOf(review.votes!!) }
  var downvoteEnabled by remember { mutableStateOf(false) }
  var upvoteEnabled by remember { mutableStateOf(false) }

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
        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = { votes -= 1 }, enabled = downvoteEnabled) {
            Icon(Icons.Filled.ArrowDownward, contentDescription = "Down vote")
          }
          Text(text = votes.toString(), modifier = modifier.padding(horizontal = 6.dp))

          IconButton(onClick = { votes += 1 }, enabled = upvoteEnabled) {
            Icon(Icons.Filled.ArrowUpward, contentDescription = "Up vote")
          }
        }
      }
    }
    Text(text = review.content!!)
  }
}
