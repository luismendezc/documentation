package com.oceloti.lemc.designlemc.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun LemcAuthScreen(
  email: String?,
  token: String?,
  onError: (String) -> Unit,
  onSuccess: () -> Unit,
  onUpdate: () -> Unit,
  onFlowCLick: () -> Unit
) {
  Column(modifier = Modifier.padding(16.dp)) {
    Text(text = "Lemc Auth Screen")
    Text(text = "Email: $email")
    Text(text = "Token: $token")

    Button(
      onClick = { onError("Something went wrong!") },
      modifier = Modifier.padding(top = 16.dp)
    ) {
      Text(text = "Simulate Error")
    }

    Button(
      onClick = onSuccess,
      modifier = Modifier.padding(top = 16.dp)
    ) {
      Text(text = "Simulate Success")
    }

    Button(
      onClick = onUpdate,
      modifier = Modifier.padding(top = 16.dp)
    ) {
      Text(text = "Simulate Update")
    }

    Button(
      onClick = onFlowCLick,
      modifier = Modifier.padding(top = 16.dp)
    ) {
      Text(text = "Flow Demo")
    }
  }
}
