package com.oceloti.lemc.designlemc.presentation.states

import org.osmdroid.util.GeoPoint


// Map screen state
data class MapState(
  val isLoading: Boolean = false,
  val center: GeoPoint = GeoPoint(51.1657, 10.4515), // Germany center
  val zoomLevel: Double = 5.0,
  val markers: List<GeoPoint> = emptyList(),
  val error: String? = null
)