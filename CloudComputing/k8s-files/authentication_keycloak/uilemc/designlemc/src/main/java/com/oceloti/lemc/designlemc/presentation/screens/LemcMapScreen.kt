package com.oceloti.lemc.designlemc.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oceloti.lemc.designlemc.presentation.actions.MapAction
import com.oceloti.lemc.designlemc.presentation.components.OSMMapView
import com.oceloti.lemc.designlemc.presentation.states.MapState
import com.oceloti.lemc.designlemc.presentation.viewmodels.MapViewModel
import org.osmdroid.util.GeoPoint

@Composable
fun LemcMapScreenRoot(viewModel: MapViewModel = viewModel()) {
  val uiState by viewModel.state.collectAsState()

  LemcMapScreen(
    state = uiState,
    onAction = { action ->
      viewModel.onAction(action)
    }
  )
}

@Composable
fun LemcMapScreen(
  state: MapState,
  onAction: (MapAction) -> Unit
) {
  LaunchedEffect(Unit) {
    onAction(MapAction.LoadMapData)
  }
  Box(modifier = Modifier.fillMaxSize()) {
    if (state.isLoading) {
      // Show loading indicator
      CircularProgressIndicator(Modifier.align(Alignment.Center))
    }

    // The actual map
    OSMMapView(
      modifier = Modifier.fillMaxSize(),
      mapState = state,
      onMapEvent = { event ->
        // We can dispatch map-related events here if needed
        onAction(event)
      }
    )

    Button(
      onClick = {
        // Add a marker in Berlin for demonstration
        onAction(
          MapAction.AddMarker(GeoPoint(52.5200, 13.4050))
        )
      },
      modifier = Modifier.align(Alignment.BottomCenter)
    ) {
      Text("Add Marker in Berlin")
    }

  }
}