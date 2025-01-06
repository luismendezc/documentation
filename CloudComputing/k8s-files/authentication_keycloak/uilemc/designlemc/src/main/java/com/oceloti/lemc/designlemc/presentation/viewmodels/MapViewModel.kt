package com.oceloti.lemc.designlemc.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oceloti.lemc.designlemc.presentation.actions.MapAction
import com.oceloti.lemc.designlemc.presentation.states.MapState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

  private val _state = MutableStateFlow(MapState())
  val state: StateFlow<MapState> = _state

  fun onAction(intent: MapAction) {
    when (intent) {
      is MapAction.LoadMapData -> {
        loadMapData()
      }
      is MapAction.AddMarker -> {
        addMarker(intent.point)
      }
      is MapAction.ChangeCenter -> {
        changeCenter(intent.point)
      }
      is MapAction.ChangeZoom -> {
        changeZoom(intent.zoom)
      }
    }
  }

  private fun loadMapData() {
    viewModelScope.launch {
      // Simulate a loading scenario:
      _state.value = _state.value.copy(isLoading = true)

      // Here, you might fetch data from a repository or do network calls
      // For simplicity, we just simulate a delay or dummy logic:
      // delay(1000) // if you want a fake load

      // On success:
      _state.value = _state.value.copy(isLoading = false)
    }
  }

  private fun addMarker(point: org.osmdroid.util.GeoPoint) {
    val updatedMarkers = _state.value.markers + point
    _state.value = _state.value.copy(markers = updatedMarkers)
  }

  private fun changeCenter(point: org.osmdroid.util.GeoPoint) {
    _state.value = _state.value.copy(center = point)
  }

  private fun changeZoom(zoom: Double) {
    _state.value = _state.value.copy(zoomLevel = zoom)
  }
}