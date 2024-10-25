package com.example.labimagecompressor.util.interfaces

import android.net.Uri

interface ImageCompressor {
    suspend fun compressImage(
        contentUri: Uri,
        compressionThreshold: Long
    ): ByteArray?
}