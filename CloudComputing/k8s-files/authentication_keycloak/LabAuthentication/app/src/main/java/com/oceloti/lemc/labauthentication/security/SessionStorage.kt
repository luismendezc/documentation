package com.oceloti.lemc.labauthentication.security

import com.oceloti.lemc.labauthentication.network.LabToken

interface SessionStorage {
  suspend fun get(): LabToken?
  suspend fun set(info: LabToken?)
}