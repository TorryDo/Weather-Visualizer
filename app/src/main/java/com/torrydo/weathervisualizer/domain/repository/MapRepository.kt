package com.torrydo.weathervisualizer.domain.repository

import com.google.android.gms.maps.model.LatLng
import java.util.*


interface MapRepository {

    @Deprecated("the latlng from weatherAPI isn't exactly, which caused the result not so reliable")
    suspend fun getCityNameByLatLng(latLng: LatLng, locale: Locale): String

}