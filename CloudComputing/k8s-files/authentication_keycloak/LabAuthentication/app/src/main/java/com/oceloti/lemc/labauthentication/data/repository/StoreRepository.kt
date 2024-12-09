package com.oceloti.lemc.labauthentication.data.repository

import com.oceloti.lemc.labauthentication.network.networkinterfaces.StoreApi
import com.oceloti.lemc.labauthentication.network.responsemodels.StoreResponseModel

/**
 * Repository for store-related operations.
 *
 * @property api The StoreApi instance for secure API calls.
 */
class StoreRepository(private val api: StoreApi) {

  /**
   * Retrieves the list of stores.
   *
   * @return A list of stores.
   */
  suspend fun getStores(): List<StoreResponseModel> {
    return api.getStores()
  }
}