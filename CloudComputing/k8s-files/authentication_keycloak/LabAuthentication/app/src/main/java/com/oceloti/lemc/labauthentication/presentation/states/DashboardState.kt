package com.oceloti.lemc.labauthentication.presentation.states

import com.oceloti.lemc.labauthentication.network.responsemodels.StoreResponseModel
import com.oceloti.lemc.labauthentication.presentation.models.LabUser
import kotlin.collections.List

data class DashboardState(
  val labUser: LabUser? = null,
  val stores: List<StoreResponseModel> = listOf<StoreResponseModel>()
)