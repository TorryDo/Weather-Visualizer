package com.torrydo.weathervisualizer.ui.screen.weather_map

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateMapOf
import com.google.android.gms.maps.model.LatLng
import com.torrydo.weathervisualizer.common.base.BaseViewModel
import com.torrydo.weathervisualizer.common.model.onSuccess
import com.torrydo.weathervisualizer.domain.holder.WeatherInfoStateHolder
import com.torrydo.weathervisualizer.domain.repository.WeatherRepository
import com.torrydo.weathervisualizer.domain.weather.WeatherData
import com.torrydo.weathervisualizer.domain.weather.WeatherInfo
import com.torrydo.weathervisualizer.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

// IDK why orbit mvi doesn't working here, couldn't call reducer or postSideEffect. it looks like a bug
class WeatherMapViewModel(
    private val weatherInfoStateHolder: WeatherInfoStateHolder,
    private val weatherRepository: WeatherRepository
) : BaseViewModel() {

//    val curLatLng = MutableStateFlow<LatLng?>(null)

    val curWeather = MutableStateFlow<WeatherData?>(null)

    val markers = mutableStateMapOf<LatLng, Bitmap?>()

//    val weathers = mutableStateListOf<WeatherData>()

    val state = MutableStateFlow(WeatherMapState())

    init {

        ioScope {
            val f = weatherInfoStateHolder.state.first()
            val cur = f.currentWeather ?: return@ioScope
            curWeather.value = cur

            state.value = state.value.copy(
                weathers = state.value.weathers.plus(cur)
            )

        }
    }

    fun onMapCLick(latLng: LatLng, onSuccess: (WeatherData) -> Unit) {

        fun onSuccess(weatherInfo: WeatherInfo) {
            val cur = weatherInfo.currentWeather ?: return
            // onsucces must run first
            onSuccess(cur)
            state.value = state.value.copy(
                weathers = state.value.weathers.plus(cur)
            )
        }

        ioScope {
            weatherRepository.getWeatherData(
                lat = latLng.latitude,
                long = latLng.longitude
            ).onSuccess { it?.let { onSuccess(it) } }
        }


    }

    fun removeWeatherData(latLng: LatLng) {
        Logger.d("check remove: ${latLng}")
        val pos = state.value.weathers.indexOfFirst { it.getLatLng() == latLng }
        if (pos > 0) {

            markers.remove(latLng)

            state.value.weathers.let {
                state.value = state.value.copy(
                    weathers = it.minusElement(it[pos])
                )
            }

            Logger.d("remove at: ${latLng}")

        }

    }


}