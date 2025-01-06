package com.oceloti.lemc.uilemc


import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.Serializable

/**
 * This mimics VerimiResponseModel but for our own library's structure.
 */
data class LemcResponseModel(
  val operation: LemcSdkOperation? = null,
  val traceId: String = "",
  val update: Update? = null,
  val success: Success? = null,
  val error: Error? = null
) : Serializable {

  data class Update(
    val type: String,
    val status: String,
    val description: String? = null
  ) : Serializable

  data class Success(
    val status: String,
    val redirectUrl: String? = null,
    val singleUserMode: Boolean? = null,
    val isDeviceSecure: Boolean? = null,
    val is2FATransactionActive: Boolean? = null,
    val takId: String? = null,
    val sealOneId: String? = null
  ) : Serializable

  data class Error(
    val type: String,
    val errorClass: String,
    val title: String? = null,
    val message: String? = null,
    val errorCode: String? = null
  ) : Serializable

  // Optional helper function to parse from JSON:
  companion object {
    private const val TAG = "LemcResponseModel"

    fun fromMessage(jsonMessage: String): LemcResponseModel? {
      return try {
        Gson().fromJson(jsonMessage, LemcResponseModel::class.java)
      } catch (e: JsonSyntaxException) {
        null // or log an error
      }
    }
  }

  // A helper property to see if update.status == "IN_PROGRESS"
  val isInProgress: Boolean
    get() = update?.status == "IN_PROGRESS"

  // A helper property to see if success != null and success.status == "SUCCEEDED"
  private val isSucceeded: Boolean
    get() = success?.status == "SUCCEEDED"

  val hasError: Boolean
    get() = error != null
}

/**
 * A sealed class for specifying the operation type, similar to VerimiSdkOperation.
 */
sealed class LemcSdkOperation {
  object AUTH : LemcSdkOperation()
  object BLOCK_ACCOUNT : LemcSdkOperation()
  // ... other operations as needed
}