package com.example.lab_testing_coroutines.logic.auth

import com.example.lab_testing_coroutines.KikeError

interface AuthenticationServiceInterface {
    suspend fun enrichError(error: KikeError)
    suspend fun getTakId(): IdOperationResult?
    suspend fun getSealOneId(): IdOperationResult?
}