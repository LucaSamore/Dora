package com.example.dora.ui.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dora.R

@Composable
internal fun ReviewListItem(modifier: Modifier) {
  val context = LocalContext.current

  Column(
    modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
  ) {
    Row(
      modifier = modifier.fillMaxWidth().padding(vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.Top,
      ) {
        AsyncImage(
          model =
            ImageRequest.Builder(context)
              .data(ResourcesCompat.getDrawable(context.resources, R.drawable.default_avatar, null))
              .crossfade(true)
              .build(),
          contentDescription = "Profile picture",
          modifier =
            Modifier.size(64.dp, 64.dp)
              .clip(RoundedCornerShape(100))
              .border(2.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(100)),
          contentScale = ContentScale.Crop,
        )

        Column(
          modifier = modifier.padding(horizontal = 12.dp),
          verticalArrangement = Arrangement.Top,
          horizontalAlignment = Alignment.Start
        ) {
          Text(
            text = "Mario Rossi",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(vertical = 4.dp),
          )
          Row { repeat(5) { Icon(Icons.Filled.Star, contentDescription = "Rating star") } }
        }
      }

      Row {
        Icon(Icons.Filled.ArrowDownward, contentDescription = "Down vote")
        Text(text = "10", modifier = modifier.padding(horizontal = 6.dp))
        Icon(Icons.Filled.ArrowUpward, contentDescription = "Up vote")
      }
    }
    Text(text = "This is some crappy content just to see how it looks like")
  }
}
