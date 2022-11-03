package com.torrydo.weathervisualizer.ui.screen.weather_map

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.torrydo.weathervisualizer.common.base.BaseViewModel
import com.torrydo.weathervisualizer.common.model.onError
import com.torrydo.weathervisualizer.common.model.onSuccess
import com.torrydo.weathervisualizer.domain.holder.WeatherInfoStateHolder
import com.torrydo.weathervisualizer.domain.repository.MapRepository
import com.torrydo.weathervisualizer.domain.repository.MarkerRepository
import com.torrydo.weathervisualizer.domain.repository.WeatherRepository
import com.torrydo.weathervisualizer.domain.weather.WeatherData
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.util.*

class WeatherMapViewModel(
    private val weatherInfoStateHolder: WeatherInfoStateHolder,
    private val weatherRepository: WeatherRepository,
    private val markerRepository: MarkerRepository,
    private val mapRepository: MapRepository
) : ContainerHost<WeatherMapState, WeatherMapSideEffect>, BaseViewModel() {

    val markerAndBmMap = mutableStateMapOf<LatLng, Bitmap?>()

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

                intent {
                    reduce {
                        state.copy(
                            weathers = newList
                        )
                    }
                    postSideEffect(WeatherMapSideEffect.Toast("updated"))
                }


            }.launchIn(viewModelScope)

    }

    fun addMarker(latLng: LatLng) = intent {
        weatherRepository.getWeatherData(
            lat = latLng.latitude,
            long = latLng.longitude
        ).onSuccess {
            val cur = it?.currentWeather ?: return@onSuccess
            markerRepository.insertMarker(cur.lat, cur.lng)
        }
    }

    fun removeMarker(latLng: LatLng) = intent {
        val pos = state.weathers.indexOfFirst { it.getLatLng() == latLng }
        if (pos > 0) {
            markerRepository.deleteMarkerByLatLng(latLng.latitude, latLng.longitude)
        }else{
            postSideEffect(WeatherMapSideEffect.AlertCanNotRemoveCurrentMarker)
        }
    }

    @Deprecated("click to see the reason!")
    suspend fun getCityName(latLng: LatLng, locale: Locale): String {
        return mapRepository.getCityNameByLatLng(latLng, locale)
    }

    override val container = container<WeatherMapState, WeatherMapSideEffect>(WeatherMapState())


}