package com.example.labdaggerhiltcompose.domain.repository

interface MyRepository {
    suspend fun doNetworkCall()
}