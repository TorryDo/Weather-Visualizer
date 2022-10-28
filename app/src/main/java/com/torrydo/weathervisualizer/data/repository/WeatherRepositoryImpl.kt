@file:Suppress("SpellCheckingInspection")

package com.torrydo.weathervisualizer.data.repository

import com.torrydo.weathervisualizer.common.model.Resource
import com.torrydo.weathervisualizer.data.mapper.toWeatherInfo
import com.torrydo.weathervisualizer.data.remote.OpenMeteoWeatherApi
import com.torrydo.weathervisualizer.domain.repository.WeatherRepository
import com.torrydo.weathervisualizer.domain.weather.WeatherInfo

class WeatherRepositoryImpl(
    private val openMeteoWeatherApi: OpenMeteoWeatherApi
) : WeatherRepository {

    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {

        val rs = openMeteoWeatherApi.getWeather(lat, long)
        if (rs.isError() || rs.data == null) {
            return Resource.Error(rs.exception)
        }

        return Resource.Success(rs.data.toWeatherInfo())
    }


}