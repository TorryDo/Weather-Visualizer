package com.torrydo.weathervisualizer.domain.weather

import java.time.LocalDateTime

data class WeatherData(
    val time: LocalDateTime,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val weatherType: WeatherType,
    val pressureMsl: Double
)