package com.example.labimagecompressor.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.labimagecompressor.util.interfaces.FileManager
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class FileManagerImpl(
    private val context: Context,
    private val dispatcher: DispatcherProvider = StandardDispatcher()
): FileManager {

    override suspend fun saveImage(
        contentUri: Uri,
        fileName: String
    ) {
        withContext(dispatcher.io) {
            context
                .contentResolver
                .openInputStream(contentUri)
                ?.use { inputStream ->
                    context
                        //.openFileOutput(fileName, Context.MODE_PRIVATE)
                        .openFileOutput(fileName, Context.MODE_PRIVATE)
                        .use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                }

        }
    }

    /*

    override suspend fun saveImage(
        bytes: ByteArray,
        fileName: String
    ) {
        withContext(dispatcher.io) {
            context
                .openFileOutput(fileName, Context.MODE_PRIVATE)
                .use { outputStream ->
                    outputStream.write(bytes)
                }
        }
    }*/

    override suspend fun saveImage(
        bytes: ByteArray,
        fileName: String
    ):Uri? {
        return withContext(dispatcher.io) {
            try {
                // For Android Q (API 29) and above, save using MediaStore
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }

                    val uri: Uri? = context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )

                    uri?.let {
                        context.contentResolver.openOutputStream(it).use { outputStream ->
                            outputStream?.write(bytes)
                        }
                    }
                    uri
                } else {
                    // For Android versions below Q, save to external storage path directly
                    val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    if (!picturesDir.exists()) {
                        picturesDir.mkdirs()
                    }
                    val file = File(picturesDir, fileName)
                    file.outputStream().use { outputStream ->
                        outputStream.write(bytes)
                    }
                    Uri.fromFile(file)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

}


}