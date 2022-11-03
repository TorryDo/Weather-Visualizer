package com.torrydo.weathervisualizer.ui.screen.current_weather

import com.torrydo.weathervisualizer.common.base.BaseViewModel
import com.torrydo.weathervisualizer.domain.holder.WeatherInfoStateHolder
import com.torrydo.weathervisualizer.domain.repository.MapRepository
import com.torrydo.weathervisualizer.ui.screen.weather_map.getLatLng
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.util.*

class CurrentWeatherViewModel(
    private val weatherInfoStateHolder: WeatherInfoStateHolder,
    private val mapRepository: MapRepository
) : ContainerHost<CurrentWeatherState, CurrentWeatherSideEffect>, BaseViewModel() {

    override val container =
        container<CurrentWeatherState, CurrentWeatherSideEffect>(CurrentWeatherState())

    init {
        ioScope {
            weatherInfoStateHolder.state.collectLatest {
                val cur = it.currentWeather ?: return@collectLatest
                val locName = mapRepository.getCityNameByLatLng(cur.getLatLng(), Locale.getDefault())
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
                            locationName = locName
                        )
                    }
                }
            }
        }
    }

    fun navigateToNext7DaysScreen(){
        intent {
            postSideEffect(CurrentWeatherSideEffect.NavigateToNext7DaysScreen)
        }
    }


}