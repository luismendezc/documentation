package com.oceloti.lemc.designlemc.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.oceloti.lemc.designlemc.presentation.actions.MapAction
import com.oceloti.lemc.designlemc.presentation.states.MapState
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

/**
 * Wraps an osmdroid MapView inside a Compose AndroidView.
 * Takes [mapState] to configure or update the map (center, zoom, markers),
 * and uses [onMapEvent] if we want to dispatch events upward.
 */
@Composable
fun OSMMapView(
  modifier: Modifier = Modifier,
  mapState: MapState,
  onMapEvent: (MapAction) -> Unit
) {
  val context = LocalContext.current
  AndroidView(
    modifier = modifier,
    factory = { context ->
      MapView(context).apply {
        setTileSource(TileSourceFactory.MAPNIK)
        setMultiTouchControls(true)
      }
    },
    update = { mapView ->
      // Update map position
      mapView.controller.setCenter(mapState.center)
      mapView.controller.setZoom(mapState.zoomLevel)

      // Clear existing overlays (markers, etc.) first if needed
      mapView.overlays.clear()

      // Add markers from our state
      mapState.markers.forEach { point ->
        val marker = Marker(mapView).apply {
          position = point
          title = "Marker @ (${point.latitude}, ${point.longitude})"
        }
        mapView.overlays.add(marker)
      }


      // Refresh the map if needed
      mapView.invalidate()
    }
  )
}