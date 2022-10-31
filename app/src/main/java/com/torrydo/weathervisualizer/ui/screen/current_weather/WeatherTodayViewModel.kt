package com.torrydo.weathervisualizer.ui.screen.current_weather

import com.torrydo.weathervisualizer.common.base.BaseViewModel
import com.torrydo.weathervisualizer.domain.holder.WeatherInfoStateHolder
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class WeatherTodayViewModel(
    private val weatherInfoStateHolder: WeatherInfoStateHolder
) : ContainerHost<WeatherTodayState, WeatherTodaySideEffect>, BaseViewModel() {

    override val container =
        container<WeatherTodayState, WeatherTodaySideEffect>(WeatherTodayState())

    init {
        ioScope {
            weatherInfoStateHolder.state.collectLatest {
                val cur = it.currentWeather ?: return@collectLatest
                intent {
                    reduce {
                        state.copy(
                            temperature = cur.temperature,
                            time = cur.time.hour.toDouble(),
                            humidity = cur.humidity,
                            windSpeed = cur.windSpeed,
                            weatherType = cur.weatherType,
                            pressure = cur.pressureMsl.toString(),
                            weatherPerHour = it.weatherPerDay[0] ?: emptyList(),
                            locationName = "fakeCity"
                        )
                    }
                }
            }
        }
    }

    fun navigateToNext7DaysScreen(){
        intent {
            postSideEffect(WeatherTodaySideEffect.NavigateToNext7DaysScreen)
        }
    }

}