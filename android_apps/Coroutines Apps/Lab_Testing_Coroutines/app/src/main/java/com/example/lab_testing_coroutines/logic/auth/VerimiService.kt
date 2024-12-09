package com.example.lab_testing_coroutines.logic.auth

import android.os.CancellationSignal
import android.util.Log

class VerimiService: VerimiServiceInterface {
    override fun getVerimiTakId(signal:CancellationSignal, callback: (ResultOperationCallback<IdOperationResult>) -> Unit) {
        Log.d(TAG, "getVerimiTakId")
        if (signal.isCanceled) {
            Log.d(TAG, "Operation was cancelled before starting")
            return
        }
        callback(ResultOperationCallback.Success(IdOperationResult(takId = "takId")))
    }

    override fun getVerimiSealOneId(callback: (ResultOperationCallback<IdOperationResult>) -> Unit) {
        Log.d(TAG, "getVerimiSealOneId")
        callback(ResultOperationCallback.Success(IdOperationResult(sealOneId = "sealOneId")))
    }

    companion object {
        val TAG = VerimiService::class.java.simpleName
    }
}