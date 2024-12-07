package com.oceloti.lemc.labauthentication.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.labauthentication.presentation.viewmodel.MainViewModel

@Composable
fun TokensScreen(viewModel: MainViewModel) {
  Text(
    text = "Loading tokens...",
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
  )
}


