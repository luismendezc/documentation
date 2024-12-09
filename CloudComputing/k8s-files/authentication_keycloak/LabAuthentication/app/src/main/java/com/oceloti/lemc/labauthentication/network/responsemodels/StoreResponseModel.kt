package com.oceloti.lemc.labauthentication.network.responsemodels

import com.google.gson.annotations.SerializedName

data class StoreResponseModel(
  @SerializedName("store_id") val storeId: Int,
  val name: String,
  val location: String
)