package com.oceloti.lemc.masterapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.FileUtils
import java.io.File

class ScreenshotProvider : ContentProvider() {

  companion object {
    const val AUTHORITY = "com.oceloti.lemc.masterapp.provider"
    val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/screenshots")
  }

  override fun onCreate(): Boolean = true

  override fun query(
    uri: Uri, projection: Array<out String>?, selection: String?,
    selectionArgs: Array<out String>?, sortOrder: String?
  ): Cursor? {
    val context = context ?: return null
    val screenshotDir = File(context.filesDir, "screenshots")

    val files = screenshotDir.listFiles()?.sortedByDescending { it.lastModified() } ?: emptyList()
    val matrixCursor = android.database.MatrixCursor(arrayOf("_id", "path"))

    files.forEachIndexed { index, file ->
      matrixCursor.addRow(arrayOf(index, file.absolutePath))
    }

    return matrixCursor
  }

  override fun getType(uri: Uri): String? = "vnd.android.cursor.dir/vnd.$AUTHORITY.screenshots"

  override fun insert(uri: Uri, values: ContentValues?): Uri? = null
  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
  override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
