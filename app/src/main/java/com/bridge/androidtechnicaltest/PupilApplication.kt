package com.bridge.androidtechnicaltest

import android.app.Application
import com.bridge.androidtechnicaltest.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PupilApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PupilApplication)
            modules(appModule)
        }
    }
}