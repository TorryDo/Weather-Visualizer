package com.torrydo.weathervisualizer.domain.use_case

import com.torrydo.weathervisualizer.common.error.LocationNotFoundError
import com.torrydo.weathervisualizer.common.model.Resource
import com.torrydo.weathervisualizer.domain.location.LocationTracker
import com.torrydo.weathervisualizer.domain.repository.WeatherRepository
import com.torrydo.weathervisualizer.domain.weather.WeatherInfo
import com.torrydo.weathervisualizer.utils.Logger

class GetCurrentWeatherUseCase(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker
) {

    suspend operator fun invoke(): Resource<WeatherInfo> {
        val loc = locationTracker.getCurrentLocation()
            ?: return Resource.Error(LocationNotFoundError())

        val lat = loc.latitude
        val lon = loc.longitude

        return weatherRepository.getWeatherData(lat, lon)

    }
}