package com.example.lab_testing_coroutines.logic.auth

import android.os.CancellationSignal

interface VerimiServiceInterface {
    fun getVerimiTakId(signal:CancellationSignal, callback: (ResultOperationCallback<IdOperationResult>)-> Unit)
    fun getVerimiSealOneId(callback: (ResultOperationCallback<IdOperationResult>)-> Unit)
}