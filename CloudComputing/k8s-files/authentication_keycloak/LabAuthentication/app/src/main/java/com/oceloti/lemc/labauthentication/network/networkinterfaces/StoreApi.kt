package com.oceloti.lemc.labauthentication.network.networkinterfaces

import com.oceloti.lemc.labauthentication.network.responsemodels.StoreResponseModel
import retrofit2.http.GET

/**
 * Interface for secure store-related API operations.
 */
interface StoreApi {

  /**
   * Retrieves the list of stores from the secure API.
   *
   * @return A list of stores.
   */
  @GET("api/secure")
  suspend fun getStores(): List<StoreResponseModel>
}
