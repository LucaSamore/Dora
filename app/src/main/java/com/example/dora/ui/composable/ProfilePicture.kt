package com.example.dora.ui.composable

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dora.R

@Composable
internal fun ProfilePicture(
  image: Any? = null,
  context: Context,
  defaultAvatar: Boolean = true,
  size: Pair<Dp, Dp> = Pair(128.dp, 128.dp)
) {
  val avatar = ResourcesCompat.getDrawable(context.resources, R.drawable.default_avatar, null)

  AsyncImage(
    model =
      ImageRequest.Builder(context)
        .data(if (defaultAvatar) avatar else image)
        .crossfade(true)
        .build(),
    contentDescription = "Profile picture",
    modifier =
      Modifier.size(size.first, size.second)
        .clip(RoundedCornerShape(100))
        .border(2.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(100)),
    contentScale = ContentScale.Crop,
  )
}
