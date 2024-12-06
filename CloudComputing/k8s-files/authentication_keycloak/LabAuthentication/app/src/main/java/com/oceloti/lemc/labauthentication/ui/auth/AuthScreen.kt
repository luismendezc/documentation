package com.oceloti.lemc.labauthentication.ui.auth

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oceloti.lemc.labauthentication.R
import com.oceloti.lemc.labauthentication.ui.ObserveAsEvents
import com.oceloti.lemc.labauthentication.ui.components.ActionButton
import com.oceloti.lemc.labauthentication.ui.components.GradientBackground
import com.oceloti.lemc.labauthentication.ui.components.OutlinedActionButton
import com.oceloti.lemc.labauthentication.ui.theme.LabAuthenticationTheme
import com.oceloti.lemc.labauthentication.viewmodel.AuthViewModel
import com.oceloti.lemc.labauthentication.viewmodel.events.AuthEvent
import com.oceloti.lemc.labauthentication.viewmodel.states.AuthState
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreenRoot(
  viewModel: AuthViewModel = koinViewModel<AuthViewModel>()
) {
  val context = LocalContext.current

  ObserveAsEvents(viewModel.events) { event ->
    when (event) {
      is AuthEvent.Error -> {
        Toast.makeText(
          context, event.error.asString(context),
          Toast.LENGTH_LONG
        ).show()
      }

      is AuthEvent.OauthLoginUrlGenerated -> {
        val uri = Uri.parse(event.url)
        val customTabsIntent = CustomTabsIntent.Builder()
          .setShowTitle(false)
          .build()
        customTabsIntent.launchUrl(context, uri)
      }
    }
  }

  AuthScreen(
    state = viewModel.state,
    onAction = { action ->
      viewModel.onAction(action)
    }
  )
}

@Composable
fun AuthScreen(
  state: AuthState,
  onAction: (AuthAction) -> Unit
) {

  GradientBackground {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      contentAlignment = Alignment.Center
    ) {
      LabAuthenticationLogoVertical()
    }
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .padding(bottom = 48.dp)
    ) {
      Text(
        text = stringResource(id = R.string.welcome_to_lab_authentication),
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 20.sp
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = stringResource(id = R.string.lab_authentication_description),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onBackground,
      )
      Spacer(modifier = Modifier.height(8.dp))
      ActionButton(
        text = stringResource(id = R.string.sign_in),
        isLoading = false,
        enabled = !state.isLoginInProgress,
        onClick = {
          onAction(AuthAction.OnSignInClick)
        },
        modifier = Modifier
          .fillMaxWidth()
      )
      Spacer(modifier = Modifier.height(8.dp))
      OutlinedActionButton(
        text = stringResource(id = R.string.sign_up),
        isLoading = false,
        enabled = !state.isRegisterInProgress,
        onClick = {
          onAction(AuthAction.OnSignUpClick)
        },
        modifier = Modifier
          .fillMaxWidth()
      )
    }
  }
}

@Composable
private fun LabAuthenticationLogoVertical(
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      imageVector = ImageVector.vectorResource(id = R.drawable.test),
      contentDescription = "Logo",
      tint = MaterialTheme.colorScheme.onSurface,
      modifier = Modifier.size(100.dp)
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
      text = stringResource(id = R.string.labAuthentication),
      fontSize = 24.sp,
      fontWeight = FontWeight.Medium,
      color = MaterialTheme.colorScheme.onBackground
    )
  }
}

@Preview(apiLevel = 33)
@Composable
private fun AuthScreenPreview() {
  val mainState = AuthState()
  LabAuthenticationTheme {
    AuthScreen(
      state = mainState,
      onAction = {

      }
    )
  }
}