package com.oceloti.lemc.masterapp.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel : ViewModel() {

  private val _screenshots = MutableLiveData<List<File>>()
  val screenshots: LiveData<List<File>> = _screenshots

  fun takeScreenshot(view: View, context: Context, onScreenshotSaved: (File?) -> Unit) {
    view.post {
      if (view.width > 0 && view.height > 0) {
        Log.d("DetailViewModel", "Taking Screenshot...")
        val screenshot = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshot)
        view.draw(canvas)

        val file = saveBitmapToFile(screenshot, context)
        loadScreenshots(context) // Refresh list
        onScreenshotSaved(file)
      } else {
        Log.e("DetailViewModel", "View width or height is 0. Cannot take screenshot.")
        onScreenshotSaved(null)
      }
    }
  }

  private fun saveBitmapToFile(bitmap: Bitmap, context: Context): File? {
    val dir = File(context.filesDir, "screenshots")
    if (!dir.exists()) dir.mkdirs()

    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val file = File(dir, "screenshot_$timestamp.png")

    return try {
      val outputStream = FileOutputStream(file)
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
      outputStream.flush()
      outputStream.close()
      Log.d("DetailViewModel", "Screenshot saved: ${file.absolutePath}")
      file
    } catch (e: Exception) {
      Log.e("DetailViewModel", "Error saving screenshot", e)
      null
    }
  }

  fun loadScreenshots(context: Context) {
    val dir = File(context.filesDir, "screenshots")
    val files = dir.listFiles()?.sortedByDescending { it.lastModified() } ?: emptyList()
    _screenshots.postValue(files)
  }
}
