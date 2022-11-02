package com.torrydo.weathervisualizer.ui.screen.weather_map

import com.google.android.gms.maps.model.LatLng
import com.torrydo.weathervisualizer.domain.weather.WeatherData

data class WeatherMapState(

    val weathers: List<WeatherData> = emptyList(),
//    val str: String = ""
)

sealed class WeatherMapSideEffect {

    data class Toast(val message: String) : WeatherMapSideEffect()

    object OnMapClick : WeatherMapSideEffect()

}

// ext

fun WeatherData.toLatLngPair(): Pair<LatLng, WeatherData> {
    return Pair(LatLng(lat, lng), this)
}

fun WeatherData.getLatLng(): LatLng {
    return LatLng(lat, lng)
}

fun WeatherData.isSameAs(latLng: LatLng) = lat == latLng.latitude && lng == latLng.longitude