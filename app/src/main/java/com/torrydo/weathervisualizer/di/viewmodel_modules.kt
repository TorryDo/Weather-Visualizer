package com.torrydo.weathervisualizer.di

import com.torrydo.weathervisualizer.ui.screen.weather_today.WeatherTodayViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel_modules = module {
    viewModel { WeatherTodayViewModel(get()) }
}