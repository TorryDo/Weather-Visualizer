package com.torrydo.weathervisualizer.data.repository

import android.content.Context
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.maps.model.LatLng
import com.torrydo.weathervisualizer.domain.repository.MapRepository
import kotlinx.coroutines.CompletableDeferred
import java.util.*

class MapRepositoryImpl(
    private val context: Context
) : MapRepository {
    override suspend fun getCityNameByLatLng(latLng: LatLng, locale: Locale): String {
        if (Build.VERSION.SDK_INT >= 33) {
            val rs = CompletableDeferred<String>()
            Geocoder(context, locale).getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            ) {
                rs.complete(it[0].locality)
            }
            return rs.await()
        } else {
            val addressList = Geocoder(context, locale).getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            )
            return addressList?.get(0)?.getAddressLine(0) ?: "here"
        }
    }
}