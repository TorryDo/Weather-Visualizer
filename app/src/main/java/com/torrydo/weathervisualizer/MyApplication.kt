package com.torrydo.weathervisualizer

import android.app.Application
import com.torrydo.weathervisualizer.di.repository_modules
import com.torrydo.weathervisualizer.di.stateHolder_modules
import com.torrydo.weathervisualizer.di.useCase_modules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
//                    mainModules,
                    useCase_modules,
//                    viewModelModules,
                    repository_modules,
                    stateHolder_modules
                )
            )
        }

    }

}