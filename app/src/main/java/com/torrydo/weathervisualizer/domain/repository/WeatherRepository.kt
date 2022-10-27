package com.torrydo.weathervisualizer.domain.repository

import com.torrydo.weathervisualizer.common.model.Resource
import com.torrydo.weathervisualizer.domain.weather.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
}