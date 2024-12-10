package com.oceloti.lemc.labauthentication.presentation.ui.views.auth

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
import com.oceloti.lemc.labauthentication.presentation.actions.AuthAction
import com.oceloti.lemc.labauthentication.presentation.events.AuthEvent
import com.oceloti.lemc.labauthentication.presentation.states.AuthState
import com.oceloti.lemc.labauthentication.presentation.ui.components.ActionButton
import com.oceloti.lemc.labauthentication.presentation.ui.components.GradientBackground
import com.oceloti.lemc.labauthentication.presentation.ui.components.OutlinedActionButton
import com.oceloti.lemc.labauthentication.presentation.ui.theme.LabAuthenticationTheme
import com.oceloti.lemc.labauthentication.presentation.util.ObserveAsEvents
import com.oceloti.lemc.labauthentication.presentation.viewmodel.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreenRoot(
  isLoggedIn: Boolean = false,
  isLocked: Boolean = false,
  viewModel: AuthViewModel = koinViewModel<AuthViewModel>()
) {
  val context = LocalContext.current
  Log.d("AuthScreenRoot", "isLoggedIn: $isLoggedIn, isLocked: $isLocked")

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
    isLocked = isLocked,
    onAction = { action ->
      viewModel.onAction(action)
    }
  )
}

@Composable
fun AuthScreen(
  state: AuthState,
  isLocked: Boolean = false,
  onAction: (AuthAction) -> Unit
) {
  GradientBackground {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      contentAlignment = Alignment.Center
    ) {
      LabAuthenticationLogoVertical(isLocked)
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
  isLocked: Boolean,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      imageVector = if(isLocked) ImageVector.vectorResource(id = R.drawable.lock) else ImageVector.vectorResource(id = R.drawable.test),
      contentDescription = "Logo",
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
  LabAuthenticationTheme() {
    AuthScreen(
      state = mainState,
      isLocked = false,
      onAction = {

      }
    )
  }
}