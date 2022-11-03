package com.torrydo.weathervisualizer.ui.screen.weather_map

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateMapOf
import com.google.android.gms.maps.model.LatLng
import com.torrydo.weathervisualizer.common.base.BaseViewModel
import com.torrydo.weathervisualizer.common.model.onSuccess
import com.torrydo.weathervisualizer.domain.holder.WeatherInfoStateHolder
import com.torrydo.weathervisualizer.domain.repository.MarkerRepository
import com.torrydo.weathervisualizer.domain.repository.WeatherRepository
import com.torrydo.weathervisualizer.domain.weather.WeatherData
import com.torrydo.weathervisualizer.domain.weather.WeatherInfo
import com.torrydo.weathervisualizer.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine

// IDK why orbit mvi doesn't working here, couldn't call reducer or postSideEffect. it looks like a bug
class WeatherMapViewModel(
    private val weatherInfoStateHolder: WeatherInfoStateHolder,
    private val weatherRepository: WeatherRepository,
    private val markerRepository: MarkerRepository
) : BaseViewModel() {

    val curWeather = MutableStateFlow<WeatherData?>(null)

    val markers = mutableStateMapOf<LatLng, Bitmap?>()

    val state = MutableStateFlow(WeatherMapState())

    init {

        ioScope {
            weatherInfoStateHolder.state.collectLatest { f ->
                val cur = f.currentWeather ?: return@collectLatest
                curWeather.value = cur

                // TODO: replace if existed
                state.value = state.value.copy(
                    weathers = state.value.weathers.plus(cur)
                )
            }
        }

        ioScope {
            markerRepository.getAllMarkers().combine(curWeather) { markerEntities, cur ->
                if (cur != null) {
                    val weathersDataFromDb = markerEntities.mapNotNull {
                        weatherRepository.getWeatherData(it.lat, it.lng).data?.currentWeather
                    }
                    val newList = mutableListOf(cur)
                    newList.addAll(weathersDataFromDb)

                    state.value = state.value.copy(
                        weathers = newList
                    )
                }
            }
        }
    }

    private fun insertMarker(latLng: LatLng) = ioScope {
        markerRepository.insertMarker(latLng.latitude, latLng.longitude)
    }

    private fun deleteMarker(latLng: LatLng) = ioScope {
        markerRepository.deleteMarkerByLatLng(latLng.latitude, latLng.longitude)
    }


    fun onMapCLick(latLng: LatLng, onSuccess: (WeatherData) -> Unit) {

        fun onSuccess(weatherInfo: WeatherInfo) {
            val cur = weatherInfo.currentWeather ?: return
            // TODO: onsucces must run firstly, this code sucks :(
            onSuccess(cur)
            state.value = state.value.copy(
                weathers = state.value.weathers.plus(cur)
            )

            insertMarker(cur.getLatLng())
        }

        ioScope {
            weatherRepository.getWeatherData(
                lat = latLng.latitude,
                long = latLng.longitude
            ).onSuccess { it?.let { onSuccess(it) } }
        }


    }

    fun removeMarker(latLng: LatLng) {
        Logger.d("check remove: ${latLng}")
        val pos = state.value.weathers.indexOfFirst { it.getLatLng() == latLng }
        if (pos > 0) {

            markers.remove(latLng)

            state.value.weathers.let {
                state.value = state.value.copy(
                    weathers = it.minusElement(it[pos])
                )
            }

            deleteMarker(latLng)

            Logger.d("remove at: ${latLng}")

        }

    }


}