package com.torrydo.weathervisualizer.di

import com.torrydo.weathervisualizer.ui.screen.current_weather.WeatherTodayViewModel
import com.torrydo.weathervisualizer.ui.screen.next7days.Next7DaysViewModel
import com.torrydo.weathervisualizer.ui.screen.weather_map.WeatherMapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel_modules = module {
    viewModel { WeatherTodayViewModel(get()) }
    viewModel { Next7DaysViewModel(get()) }
    viewModel { WeatherMapViewModel(get(), get()) }
}