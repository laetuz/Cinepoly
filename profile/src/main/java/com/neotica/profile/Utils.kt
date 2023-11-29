package com.neotica.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

fun handleSamplingAndRotationBitMap(context: Context, selectedImage: Uri): Bitmap? {
    val maxHeight = 1024
    val maxWidth = 1024
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    context.contentResolver.openInputStream(selectedImage)?.use {
        BitmapFactory.decodeStream(it, null, options)
    }

    //options.inSampleSize =

    options.inJustDecodeBounds = false
    val img = context.contentResolver.openInputStream(selectedImage)?.use {
        BitmapFactory.decodeStream(it, null, options)
    }

    return img
}