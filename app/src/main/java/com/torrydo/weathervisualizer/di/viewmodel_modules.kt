package com.torrydo.weathervisualizer.di

import com.torrydo.weathervisualizer.data.repository.MapRepositoryImpl
import com.torrydo.weathervisualizer.data.repository.MarkerRepositoryImpl
import com.torrydo.weathervisualizer.ui.screen.current_weather.CurrentWeatherViewModel
import com.torrydo.weathervisualizer.ui.screen.next7days.Next7DayViewModel
import com.torrydo.weathervisualizer.ui.screen.weather_map.WeatherMapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel_modules = module {
    viewModel {
        CurrentWeatherViewModel(
            weatherInfoStateHolder = get(),
            mapRepository = get<MapRepositoryImpl>()
        )
    }
    viewModel {
        Next7DayViewModel(
            weatherInfoStateHolder = get()
        )
    }
    viewModel {
        WeatherMapViewModel(
            weatherInfoStateHolder = get(),
            weatherRepository = get(),
            markerRepository = get<MarkerRepositoryImpl>(),
            mapRepository = get<MapRepositoryImpl>()
        )
    }
}