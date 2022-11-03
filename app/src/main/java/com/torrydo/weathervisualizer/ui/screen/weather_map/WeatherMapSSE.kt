package com.torrydo.weathervisualizer.ui.screen.weather_map

import com.torrydo.weathervisualizer.domain.weather.WeatherData

data class WeatherMapState(

    val weathers: List<WeatherData> = emptyList(),
)

sealed interface WeatherMapSideEffect {

    data class Toast(val message: String) : WeatherMapSideEffect

    object AlertCanNotRemoveCurrentMarker: WeatherMapSideEffect

    object OnMapClick : WeatherMapSideEffect

}