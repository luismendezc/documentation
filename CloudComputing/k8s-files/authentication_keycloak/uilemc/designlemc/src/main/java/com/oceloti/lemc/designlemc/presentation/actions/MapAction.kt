package com.oceloti.lemc.designlemc.presentation.actions

import org.osmdroid.util.GeoPoint

sealed class MapAction {
  object LoadMapData : MapAction()
  data class AddMarker(val point: GeoPoint) : MapAction()
  data class ChangeCenter(val point: GeoPoint) : MapAction()
  data class ChangeZoom(val zoom: Double) : MapAction()
  // More events if needed...
}