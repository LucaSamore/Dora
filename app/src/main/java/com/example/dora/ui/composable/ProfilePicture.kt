package com.example.dora.ui.composable

import android.content.Context
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dora.R

@Composable
fun ProfilePicture(image: Any? = null, context: Context, defaultAvatar: Boolean = true) {
    val avatar = ResourcesCompat.getDrawable(context.resources, R.drawable.default_avatar, null)

    AsyncImage(
        model =
            ImageRequest.Builder(context)
                .data(if (defaultAvatar) avatar else image)
                .crossfade(true)
                .build(),
        contentDescription = "profile picture",
        modifier = Modifier.size(128.dp, 128.dp).clip(RoundedCornerShape(100.dp)),
        contentScale = ContentScale.Crop,
    )
}
