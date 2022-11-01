package com.torrydo.weathervisualizer.ui.screen.weather_map

import androidx.compose.runtime.mutableStateListOf
import com.google.android.gms.maps.model.LatLng
import com.torrydo.weathervisualizer.common.base.BaseViewModel
import com.torrydo.weathervisualizer.common.model.onSuccess
import com.torrydo.weathervisualizer.domain.holder.WeatherInfoStateHolder
import com.torrydo.weathervisualizer.domain.repository.WeatherRepository
import com.torrydo.weathervisualizer.domain.weather.WeatherData
import com.torrydo.weathervisualizer.domain.weather.WeatherInfo
import com.torrydo.weathervisualizer.utils.Logger
import com.torrydo.weathervisualizer.utils.lang.roundTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import org.koin.core.KoinApplication.Companion.init
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container


class WeatherMapViewModel(
    private val weatherInfoStateHolder: WeatherInfoStateHolder,
    private val weatherRepository: WeatherRepository

) : BaseViewModel(), ContainerHost<WeatherMapState, WeatherMapSideEffect> {
    override val container: Container<WeatherMapState, WeatherMapSideEffect>
        get() = container(WeatherMapState())

    val weatherDataList = mutableStateListOf<WeatherData>()

    init {
        ioScope {
            weatherInfoStateHolder.state.collectLatest {
                if (it.currentWeather == null) return@collectLatest
                mainScope{
                    weatherDataList.add(it.currentWeather)
                }
            }
        }
    }

    fun onMapCLick(latLng: LatLng) {

        Logger.d(latLng.toString())

        suspend fun onSuccess(weatherInfo: WeatherInfo) {
            val cur = weatherInfo.currentWeather ?: return
            mainScope {
                weatherDataList.add(cur)
            }
            intent {
                postSideEffect(WeatherMapSideEffect.OnMapClick)
            }
        }

        ioScope {
            weatherRepository.getWeatherData(
                lat = latLng.latitude,
                long = latLng.longitude
            ).onSuccess { it?.let { onSuccess(it) } }
        }
    }

}