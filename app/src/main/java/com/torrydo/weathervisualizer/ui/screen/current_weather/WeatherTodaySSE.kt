package com.torrydo.weathervisualizer.ui.screen.current_weather

import com.torrydo.weathervisualizer.domain.weather.WeatherData
import com.torrydo.weathervisualizer.domain.weather.WeatherType


data class WeatherTodayState(

    val time: Double = 0.0,
    val temperature: Double = 0.0,
    val humidity: Int = 0,
    val windSpeed: Double = 0.0,
    val weatherType: WeatherType? = null,
    val pressure: String = "None",

    val weatherPerHour: List<WeatherData> = emptyList(),

    val locationName: String = "None"
)

sealed interface WeatherTodaySideEffect {

    data class Toast(val message: String) : WeatherTodaySideEffect

    object NavigateToNext7DaysScreen: WeatherTodaySideEffect

}