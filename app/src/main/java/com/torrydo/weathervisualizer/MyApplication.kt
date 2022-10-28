package com.torrydo.weathervisualizer

import android.app.Application
import com.torrydo.weathervisualizer.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    default_modules,
                    useCase_modules,
                    viewModel_modules,
                    repository_modules,
                    stateHolder_modules
                )
            )
        }

    }

}