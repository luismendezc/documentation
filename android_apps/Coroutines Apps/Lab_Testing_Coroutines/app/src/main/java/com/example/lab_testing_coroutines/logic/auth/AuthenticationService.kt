package com.example.lab_testing_coroutines.logic.auth

import android.os.CancellationSignal
import android.util.Log
import com.example.lab_testing_coroutines.KikeError
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.yield
import kotlin.coroutines.coroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthenticationService(private val verimiService: VerimiServiceInterface) :
    AuthenticationServiceInterface {
    override suspend fun enrichError(error: KikeError) {
        try {
            val takIdResult = getTakId()
            takIdResult.takId?.let {
                error.takId = it
            }
        } catch (e: Exception) {
            Log.e(TAG, "The takId operation failed", e)
            coroutineContext.ensureActive()
        }

        Log.d(TAG, "takId found ${error.takId}")
        yield()

        try {
            val sealOneIdResult = getSealOneId()
            sealOneIdResult.sealOneId?.let {
                error.sealOneId = it
            }
        } catch (e: Exception) {
            Log.e(TAG, "The sealOneId operation failed", e)
            coroutineContext.ensureActive()
        }
        Log.d(TAG, "sealOneId found ${error.sealOneId}")

    }

    override suspend fun getTakId(): IdOperationResult {
        return suspendCancellableCoroutine<IdOperationResult> { continuation ->
            val signal = CancellationSignal()
            verimiService.getVerimiTakId(signal) { takResultId ->
                if(continuation.isActive) {
                    when (takResultId) {
                        is ResultOperationCallback.Success -> {
                            continuation.resume(takResultId.data)
                        }

                        is ResultOperationCallback.Error -> {
                            continuation.resumeWithException(takResultId.throwable)
                        }
                    }
                }
            }
            continuation.invokeOnCancellation {
                signal.cancel()
                Log.d(TAG, "getTakId coroutine was cancelled and signal sent")
            }
        }.also {
            delay(2000L)
        }
    }

    override suspend fun getSealOneId(): IdOperationResult {
        return suspendCoroutine { continuation ->
            verimiService.getVerimiSealOneId { takResultId ->
                when (takResultId) {
                    is ResultOperationCallback.Success -> {
                        continuation.resume(takResultId.data)
                    }

                    is ResultOperationCallback.Error -> {
                        continuation.resumeWithException(takResultId.throwable)
                    }
                }
            }
        }.also {
            delay(2000L)
        }
    }


    companion object {
        private const val TAG = "AuthenticationService"
    }

}