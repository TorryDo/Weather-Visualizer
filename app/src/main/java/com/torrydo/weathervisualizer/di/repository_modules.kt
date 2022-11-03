package com.torrydo.weathervisualizer.di

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.torrydo.weathervisualizer.MarkerDatabase
import com.torrydo.weathervisualizer.data.remote.OpenMeteoWeatherApi
import com.torrydo.weathervisualizer.data.repository.MarkerRepositoryImpl
import com.torrydo.weathervisualizer.data.repository.WeatherRepositoryImpl
import com.torrydo.weathervisualizer.domain.repository.WeatherRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repository_modules = module {
    single<WeatherRepository> { WeatherRepositoryImpl(OpenMeteoWeatherApi()) }

    factory<AndroidSqliteDriver> {
        AndroidSqliteDriver(MarkerDatabase.Schema, androidContext(), "marker.db")
    }
    factory<MarkerDatabase> {
        MarkerDatabase(get<AndroidSqliteDriver>())
    }
    single<MarkerRepositoryImpl> {
        MarkerRepositoryImpl(
            db = get<MarkerDatabase>()
        )
    }

}