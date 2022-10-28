package com.torrydo.weathervisualizer.di

import com.torrydo.weathervisualizer.domain.use_case.GetCurrentWeatherUseCase
import org.koin.dsl.module

val useCase_modules = module {
    single { GetCurrentWeatherUseCase(get(), get()) }
}