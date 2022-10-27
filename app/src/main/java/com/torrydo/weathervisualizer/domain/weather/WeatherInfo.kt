package com.torrydo.weathervisualizer.domain.weather

data class WeatherInfo(
    val weatherPerDays: Map<Int, List<WeatherData>>,
    val currentWeather: WeatherData
)