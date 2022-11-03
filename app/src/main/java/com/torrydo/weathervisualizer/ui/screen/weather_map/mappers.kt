package com.torrydo.weathervisualizer.ui.screen.weather_map

import com.google.android.gms.maps.model.LatLng
import com.torrydo.weathervisualizer.domain.weather.WeatherData


internal fun WeatherData.toLatLngPair(): Pair<LatLng, WeatherData> {
    return Pair(LatLng(lat, lng), this)
}

internal fun WeatherData.getLatLng() = LatLng(lat, lng)

internal fun WeatherData.isSameAs(latLng: LatLng) =
    lat == latLng.latitude && lng == latLng.longitude