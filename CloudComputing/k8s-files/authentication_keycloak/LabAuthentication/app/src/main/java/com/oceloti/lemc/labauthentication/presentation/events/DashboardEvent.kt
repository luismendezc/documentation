package com.oceloti.lemc.labauthentication.presentation.events

import com.oceloti.lemc.labauthentication.presentation.util.UiText

sealed interface DashboardEvent {
  data class Error(val error: UiText, val shouldLogout: Boolean) : DashboardEvent
}