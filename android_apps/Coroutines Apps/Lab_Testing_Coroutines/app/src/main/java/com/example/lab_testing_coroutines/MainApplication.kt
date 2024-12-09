package com.example.lab_testing_coroutines

import android.app.Application
import android.util.Log
import com.example.lab_testing_coroutines.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "startKoin")
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }

    companion object {
        internal val TAG = MainApplication::class.java.simpleName
    }
}