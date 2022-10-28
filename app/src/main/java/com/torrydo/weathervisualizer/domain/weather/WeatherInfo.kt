package com.torrydo.weathervisualizer.domain.weather

data class WeatherInfo(
    val weatherPerDay: Map<Int, List<WeatherData>>,
    val currentWeather: WeatherData?
)