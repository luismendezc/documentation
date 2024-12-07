package com.oceloti.lemc.labauthentication.presentation.events

import com.oceloti.lemc.labauthentication.presentation.util.UiText

sealed interface AuthEvent {
  data class Error(val error: UiText) : AuthEvent
  data class OauthLoginUrlGenerated(val url: String) : AuthEvent
}