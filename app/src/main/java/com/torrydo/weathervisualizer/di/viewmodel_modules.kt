package com.torrydo.weathervisualizer.di

import com.torrydo.weathervisualizer.data.repository.MarkerRepositoryImpl
import com.torrydo.weathervisualizer.ui.screen.current_weather.WeatherTodayViewModel
import com.torrydo.weathervisualizer.ui.screen.next7days.Next7DaysViewModel
import com.torrydo.weathervisualizer.ui.screen.weather_map.WeatherMapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel_modules = module {
    viewModel {
        WeatherTodayViewModel(
            weatherInfoStateHolder = get()
        )
    }
    viewModel {
        Next7DaysViewModel(
            weatherInfoStateHolder = get()
        )
    }
    viewModel {
        WeatherMapViewModel(
            weatherInfoStateHolder = get(),
            weatherRepository = get(),
            markerRepository = get<MarkerRepositoryImpl>()
        )
    }
}