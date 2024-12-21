package com.oceloti.lemc.labauthentication.presentation.ui.views.dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oceloti.lemc.labauthentication.R
import com.oceloti.lemc.labauthentication.network.responsemodels.StoreResponseModel
import com.oceloti.lemc.labauthentication.presentation.actions.DashboardAction
import com.oceloti.lemc.labauthentication.presentation.events.DashboardEvent
import com.oceloti.lemc.labauthentication.presentation.models.LabUser
import com.oceloti.lemc.labauthentication.presentation.states.DashboardState
import com.oceloti.lemc.labauthentication.presentation.ui.components.GradientBackground
import com.oceloti.lemc.labauthentication.presentation.ui.theme.LabAuthenticationGray40
import com.oceloti.lemc.labauthentication.presentation.ui.theme.LabAuthenticationTheme
import com.oceloti.lemc.labauthentication.presentation.util.ObserveAsEvents
import com.oceloti.lemc.labauthentication.presentation.util.UiText
import com.oceloti.lemc.labauthentication.presentation.viewmodel.DashboardViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreenRoot(
  onError: (title: Int?) -> Unit,
  viewModel: DashboardViewModel = koinViewModel<DashboardViewModel>()
) {
  val context = LocalContext.current
  ObserveAsEvents(viewModel.events) { event ->
    when (event) {
      is DashboardEvent.Error -> {
        if (!event.shouldLogout) {
          Toast.makeText(
            context, event.error.asString(context),
            Toast.LENGTH_LONG
          ).show()
        } else {
          if(event.error is UiText.StringResource){
            onError(event.error.id)
          }
        }
      }
    }
  }

  DashboardScreen(state = viewModel.state, onAction = { it ->
    when(it){
      DashboardAction.OnSdkClick -> {
        viewModel.showSDK()
      }
      DashboardAction.OnSignInClick -> {
        //TODO()
      }
      DashboardAction.OnSignUpClick -> {
        //TODO()
      }
    }
  })
}

@Composable
fun DashboardScreen(
  state: DashboardState,
  onAction: (DashboardAction) -> Unit,
) {
  GradientBackground {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
          .padding(top = 32.dp)
          .background(LabAuthenticationGray40)
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .weight(0.5f)
            .padding(4.dp),
          verticalArrangement = Arrangement.Center
        ) {
          if (state.labUser == null) {
            Text(
              text = stringResource(id = R.string.lab_authentication_description),
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.onBackground,
            )
          } else {
            Text(
              text = stringResource(id = R.string.welcome_to_lab_authentication),
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(8.dp))

            Text(
              text = state.labUser.id.toString() + "\n" + state.labUser.name.toString(),
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onBackground,
            )


          }
        }
        Column(
          modifier = Modifier
            .fillMaxSize()
            .weight(0.5f),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.test),
            contentDescription = "Logo",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(100.dp)
          )
        }
      }
      Spacer(Modifier.height(16.dp))
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        if (state.stores.isEmpty()) {
          CircularProgressIndicator()
        } else {
          LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
          ) {
            items(state.stores) { store ->
              Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = store.storeId.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
              )
              Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = store.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
              )
              Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = store.location,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
              )
            }
          }
        }
      }
      Spacer(Modifier.height(16.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Button(onClick = {
          onAction(DashboardAction.OnSdkClick)
        }) {
          Text(text = "Show LemcSDK")
        }
      }

    }

  }
}

@Preview
@Composable
fun DashboardScreenPreview() {
  val listOfStore = listOf<StoreResponseModel>(
    StoreResponseModel(
      storeId = 1,
      name = "Store 1",
      location = "Location"
    ), StoreResponseModel(storeId = 2, name = "Store 2", location = "Location 2")
  )
  val dashboardState = DashboardState(
    labUser = LabUser(
      id = "123456",
      name = "Luis Mendez",
      email = "lemendezc@hotmail.com",
      lastName = "Mendez",
      firstName = "Luis",
      picture = null,
      nonce = null
    ),
    stores = listOfStore
  )
  LabAuthenticationTheme {
    DashboardScreen(
      dashboardState,
      onAction = {}
    )
  }
}