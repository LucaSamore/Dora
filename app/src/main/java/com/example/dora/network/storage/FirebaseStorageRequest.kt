package com.example.dora.network.storage

import android.net.Uri

data class FirebaseStorageRequest(
    val fileUri: Uri = Uri.EMPTY,
    val reference: String,
) {
    fun fullReference() : String = "$reference/${fileUri.lastPathSegment}"
}

/*
    Reference:
    userId/profilePicture/<path-to-file>
    userId/businesses/businessId/<path-to-file>
*/