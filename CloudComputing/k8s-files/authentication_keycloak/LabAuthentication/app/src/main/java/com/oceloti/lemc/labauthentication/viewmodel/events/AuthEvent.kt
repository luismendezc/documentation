package com.oceloti.lemc.labauthentication.viewmodel.events

import com.oceloti.lemc.labauthentication.ui.UiText

sealed interface AuthEvent {
  data class Error(val error: UiText): AuthEvent
  data class OauthLoginUrlGenerated(val url: String): AuthEvent
}