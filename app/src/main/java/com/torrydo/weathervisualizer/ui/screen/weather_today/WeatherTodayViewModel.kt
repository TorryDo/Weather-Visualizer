package com.torrydo.weathervisualizer.ui.screen.weather_today

import com.torrydo.weathervisualizer.common.base.BaseViewModel
import com.torrydo.weathervisualizer.common.model.onError
import com.torrydo.weathervisualizer.common.model.onSuccess
import com.torrydo.weathervisualizer.domain.use_case.GetCurrentWeatherUseCase
import com.torrydo.weathervisualizer.domain.weather.WeatherInfo
import com.torrydo.weathervisualizer.utils.Logger
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class WeatherTodayViewModel(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) : ContainerHost<WeatherTodayState, WeatherTodaySideEffect>, BaseViewModel() {

    override val container =
        container<WeatherTodayState, WeatherTodaySideEffect>(WeatherTodayState())

    fun updateLocation() {

        fun onSuccess(weatherInfo: WeatherInfo) {
            val cur = weatherInfo.currentWeather ?: return
            intent {
                reduce {
                    state.copy(
                        temperature = cur.temperature,
                        time = cur.time.hour.toDouble(),
                        humidity = cur.humidity,
                        windSpeed = cur.windSpeed,
                        weatherType = cur.weatherType,
                        pressure = cur.pressureMsl.toString(),
                        weatherPerHour = weatherInfo.weatherPerDay[0] ?: emptyList(),
                        locationName = "fakeCity"
                    )
                }
            }
        }

        ioScope {

            getCurrentWeatherUseCase()
                .onSuccess { onSuccess(it!!) }
                .onError { exception, data ->
                    Logger.e(exception?.reason.toString())
                }
        }
    }


}