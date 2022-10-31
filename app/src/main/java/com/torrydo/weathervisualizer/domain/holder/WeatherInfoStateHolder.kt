package com.torrydo.weathervisualizer.domain.holder

import com.torrydo.weathervisualizer.domain.use_case.GetCurrentWeatherUseCase
import com.torrydo.weathervisualizer.domain.weather.WeatherInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WeatherInfoStateHolder(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) {

    private val _state = MutableStateFlow(WeatherInfo())
    val state get() = _state.asStateFlow()

    val value get() = state.value

    suspend operator fun invoke() {
        getCurrentWeatherUseCase().data?.let {
            _state.value = it
        }
    }

}