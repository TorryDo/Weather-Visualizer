package com.torrydo.weathervisualizer.di

import com.torrydo.weathervisualizer.data.remote.OpenMeteoWeatherApi
import com.torrydo.weathervisualizer.data.repository.WeatherRepositoryImpl
import com.torrydo.weathervisualizer.domain.repository.WeatherRepository
import org.koin.dsl.module

val repository_modules = module {
    single<WeatherRepository> { WeatherRepositoryImpl(OpenMeteoWeatherApi()) }
}