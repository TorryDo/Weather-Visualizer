package com.torrydo.weathervisualizer.di

import com.google.android.gms.location.LocationServices
import com.torrydo.weathervisualizer.data.location.DefaultLocationTrackerImpl
import com.torrydo.weathervisualizer.domain.location.LocationTracker
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val default_modules = module {
    single<LocationTracker> { DefaultLocationTrackerImpl(
        locationClient = LocationServices.getFusedLocationProviderClient(androidContext()),
        application = androidApplication()
    ) }
}