package com.oceloti.lemc.labtestcoroutinesflows.suspend

import kotlinx.coroutines.delay

class SuspendFuncImpl: SuspendFunc {
    override suspend fun getData(): String {
        delay(1000L)
        return "testing"
    }
}