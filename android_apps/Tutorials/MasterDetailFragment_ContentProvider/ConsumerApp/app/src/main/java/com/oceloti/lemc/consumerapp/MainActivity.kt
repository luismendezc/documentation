package com.oceloti.lemc.consumerapp

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      ConsumerAppUI(this)
    }
  }
}

@Composable
fun ConsumerAppUI(context: Context) {
  var screenshotPaths by remember { mutableStateOf<List<String>>(emptyList()) }
  LaunchedEffect(Unit) {
    screenshotPaths = getScreenshotsFromMasterApp(context)
  }

  Column(Modifier.fillMaxSize().padding(16.dp)) {
    Text(text = "Fetched Screenshots from MasterApp", style = MaterialTheme.typography.headlineMedium)

    if (screenshotPaths.isEmpty()) {
      Text("No screenshots found", Modifier.padding(8.dp))
    } else {
      LazyColumn {
        items(screenshotPaths.size) { index ->  // ✅ Pass the size of the list
          val screenshotUri = screenshotPaths[index]
          Log.d("TESTING", screenshotUri)
          Image(
            painter = rememberAsyncImagePainter(screenshotUri),
            contentDescription = "Screenshot",
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp)
              .padding(8.dp)
          )
        }
      }
    }
  }
}

@Composable
fun ScreenshotItem(path: String) {
  Image(
    painter = rememberAsyncImagePainter(path.toUri()),
    contentDescription = "Screenshot",
    modifier = Modifier
      .fillMaxWidth()
      .height(200.dp)
      .padding(8.dp),
    contentScale = ContentScale.Crop
  )
}

fun getScreenshotsFromMasterApp(context: Context): List<String> {
  val uri = Uri.parse("content://com.oceloti.lemc.masterapp.provider/screenshots")
  val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)

  val paths = mutableListOf<String>()

  cursor?.use {
    while (it.moveToNext()) {
      val path = it.getString(it.getColumnIndexOrThrow("path"))
      paths.add(path)
    }
  }

  return paths
}