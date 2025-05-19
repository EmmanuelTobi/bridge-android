package com.bridge.androidtechnicaltest.di

import com.bridge.androidtechnicaltest.data.local.PupilDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single { PupilDatabase.getDatabase(androidContext()) }
    single { get<PupilDatabase>().pupilDao() }
}