package com.example.labimagecompressor.util.interfaces

import android.net.Uri

interface FileManager {
    suspend fun saveImage(
        contentUri: Uri,
        fileName: String
    )

    suspend fun saveImage(
        bytes: ByteArray,
        fileName: String
    ): Uri?
}