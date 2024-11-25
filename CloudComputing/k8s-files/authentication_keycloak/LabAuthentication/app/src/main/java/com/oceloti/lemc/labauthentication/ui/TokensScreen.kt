package com.oceloti.lemc.labauthentication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.labauthentication.network.TokenResponse
import com.oceloti.lemc.labauthentication.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun TokensScreen(viewModel: AuthViewModel) {
  val tokens = viewModel.tokens.collectAsState().value

  if (tokens != null) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ) {
      Text(text = "Access Token: ${tokens.accessToken}")
      Text(text = "Token type: ${tokens.tokenType}")
      Text(text = "Token expiresIn: ${tokens.expiresIn}")
      Spacer(modifier = Modifier.size(30.dp))
      Text(text = "Refresh Token: ${tokens.refreshToken}")
      Text(text = "ID Token: ${tokens.idToken}")
    }
  } else {
    Text(
      text = "Loading tokens...",
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    )
  }
}


