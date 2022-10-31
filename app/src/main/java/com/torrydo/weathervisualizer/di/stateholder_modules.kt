package com.torrydo.weathervisualizer.di

import com.torrydo.weathervisualizer.domain.holder.WeatherInfoStateHolder
import org.koin.dsl.module

val stateHolder_modules = module {
    single { WeatherInfoStateHolder(get()) }
}