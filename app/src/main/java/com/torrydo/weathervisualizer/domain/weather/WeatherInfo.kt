package com.torrydo.weathervisualizer.domain.weather

data class WeatherInfo(
    val weatherPerDay: Map<Int, List<WeatherData>> = emptyMap(),
    val currentWeather: WeatherData? = null
){
    fun getWeatherCurrentDay() = weatherPerDay[0] ?: emptyList()
}