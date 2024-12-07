package com.oceloti.lemc.labauthentication.data.security

import com.oceloti.lemc.labauthentication.data.models.LabToken

interface SessionStorage {
  fun get(): LabToken?
  fun set(info: LabToken?)
  fun clear()
}