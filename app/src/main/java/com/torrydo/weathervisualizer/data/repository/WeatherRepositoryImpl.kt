package com.torrydo.weathervisualizer.data.repository

import com.torrydo.weathervisualizer.common.model.Resource
import com.torrydo.weathervisualizer.domain.repository.WeatherRepository
import com.torrydo.weathervisualizer.domain.weather.WeatherInfo

class WeatherRepositoryImpl(

): WeatherRepository {

    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
        TODO("Not yet implemented")
    }


}