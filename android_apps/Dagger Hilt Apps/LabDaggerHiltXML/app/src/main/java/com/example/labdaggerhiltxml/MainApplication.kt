package com.example.labdaggerhiltxml

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication:Application() {

    override fun onCreate() {
        super.onCreate()
    }
}