package com.torrydo.weathervisualizer.ui.screen.weather_map

data class WeatherMapState(
//    val weatherDataList: List<WeatherData> = emptyList(),
    val str: String = ""
)

sealed interface WeatherMapSideEffect {

    data class Toast(val message: String) : WeatherMapSideEffect

    object OnMapClick : WeatherMapSideEffect

}