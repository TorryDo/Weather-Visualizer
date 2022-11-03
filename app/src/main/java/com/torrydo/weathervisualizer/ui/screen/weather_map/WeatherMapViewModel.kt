package com.torrydo.weathervisualizer.ui.screen.weather_map

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.torrydo.weathervisualizer.common.base.BaseViewModel
import com.torrydo.weathervisualizer.common.model.onError
import com.torrydo.weathervisualizer.common.model.onSuccess
import com.torrydo.weathervisualizer.domain.holder.WeatherInfoStateHolder
import com.torrydo.weathervisualizer.domain.repository.MarkerRepository
import com.torrydo.weathervisualizer.domain.repository.WeatherRepository
import com.torrydo.weathervisualizer.domain.weather.WeatherData
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

// IDK why orbit mvi doesn't working here, couldn't call reducer or postSideEffect. it looks like a bug
class WeatherMapViewModel(
    private val weatherInfoStateHolder: WeatherInfoStateHolder,
    private val weatherRepository: WeatherRepository,
    private val markerRepository: MarkerRepository
) : BaseViewModel() {

    val markerAndBmMap = mutableStateMapOf<LatLng, Bitmap?>()

    val state = MutableStateFlow(WeatherMapState())

    init {

        updateWeatherListState()
    }

    private fun updateWeatherListState() = ioScope {
        weatherInfoStateHolder.state
            .combine(markerRepository.getAllMarkers()) { weatherInfo, markerEntities ->

                val cur = weatherInfo.currentWeather ?: return@combine

                val weathersDataFromDb = markerEntities.mapNotNull {
                    val rs = CompletableDeferred<WeatherData?>()
                    weatherRepository.getWeatherData(it.lat, it.lng)
                        .onSuccess {
                            rs.complete(it?.currentWeather)
                        }.onError { exception, data ->
                            rs.complete(null)
                        }
                    rs.await()
                }

                val newList = mutableListOf(cur)
                newList.addAll(weathersDataFromDb)

                state.value = state.value.copy(
                    weathers = newList
                )
            }.launchIn(viewModelScope)

    }

    fun onMapCLick(latLng: LatLng) = ioScope {
        weatherRepository.getWeatherData(
            lat = latLng.latitude,
            long = latLng.longitude
        ).onSuccess {
            val cur = it?.currentWeather ?: return@onSuccess
            markerRepository.insertMarker(cur.lat, cur.lng)
        }
    }

    fun removeMarker(latLng: LatLng) = ioScope {
        val pos = state.value.weathers.indexOfFirst { it.getLatLng() == latLng }
        if (pos > 0) {
            markerRepository.deleteMarkerByLatLng(latLng.latitude, latLng.longitude)
        }
    }


}