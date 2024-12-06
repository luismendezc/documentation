package com.oceloti.lemc.labauthentication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.labauthentication.viewmodel.MainViewModel

@Composable
fun TokensScreen(viewModel: MainViewModel) {
  Text(
    text = "Loading tokens...",
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
  )
}


