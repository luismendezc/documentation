package com.example.labdaggerhiltxml.data.repository

import android.app.Application
import android.util.Log
import com.example.labdaggerhiltxml.R
import com.example.labdaggerhiltxml.data.remote.MyApi
import com.example.labdaggerhiltxml.domain.repository.MyRepository
import javax.inject.Inject
import javax.inject.Named

class MyRepositoryImpl @Inject constructor(
    private val api: MyApi,
    private val appContext: Application,
    @Named("hello1") private val text: String
): MyRepository {

    init {
        val appName = appContext.getString(R.string.app_name)
        Log.d("MyRepositoryImpl","Hello from the repository. The app name is $appName")
        Log.d("MyRepositoryImpl","text is: $text")
    }

    override suspend fun doNetworkCall() {
        TODO("Not yet implemented")
    }

}