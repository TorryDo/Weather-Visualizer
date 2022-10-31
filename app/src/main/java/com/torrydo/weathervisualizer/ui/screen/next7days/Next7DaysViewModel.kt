package com.torrydo.weathervisualizer.ui.screen.next7days

import com.torrydo.weathervisualizer.common.base.BaseViewModel
import com.torrydo.weathervisualizer.common.model.onError
import com.torrydo.weathervisualizer.common.model.onSuccess
import com.torrydo.weathervisualizer.domain.use_case.GetCurrentWeatherUseCase
import com.torrydo.weathervisualizer.domain.weather.WeatherInfo
import com.torrydo.weathervisualizer.utils.Logger
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class Next7DaysViewModel(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) : BaseViewModel(), ContainerHost<WeatherInfo, Next7DaySideEffect> {

    override val container =
        container<WeatherInfo, Next7DaySideEffect>(WeatherInfo())

    fun updateWeatherInfo() {

        fun onSuccess(weatherInfo: WeatherInfo) {
            intent {
                reduce {
                    state.copy(
                        weatherPerDay = weatherInfo.weatherPerDay,
                        currentWeather = weatherInfo.currentWeather
                    )
                }
            }
        }

        ioScope {
            getCurrentWeatherUseCase()
                .onSuccess {
                    if (it == null) {
                        Logger.d("weather info is null")
                    }
                    onSuccess(it!!)
                }.onError { exception, data ->
                    Logger.e(exception?.reason.toString())
                }


        }
    }

    fun navigateBack() {
        intent {
            postSideEffect(Next7DaySideEffect.NavigateBack)
        }
    }

}