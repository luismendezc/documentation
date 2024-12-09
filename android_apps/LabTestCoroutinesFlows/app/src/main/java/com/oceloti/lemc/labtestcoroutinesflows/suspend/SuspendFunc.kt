package com.oceloti.lemc.labtestcoroutinesflows.suspend

interface SuspendFunc {
    suspend fun getData(): String
}