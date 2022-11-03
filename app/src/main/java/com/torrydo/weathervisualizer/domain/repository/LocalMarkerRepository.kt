package com.torrydo.weathervisualizer.domain.repository

import kotlinx.coroutines.flow.Flow
import maps.markerdb.MarkerEntity

interface LocalMarkerRepository {

    suspend fun getMarkerById(id: Long): MarkerEntity?

    suspend fun getMarkerByLatLng(lat: Double, lng: Double): MarkerEntity?

    fun getAllMarkers(): Flow<List<MarkerEntity>>

    suspend fun insertMarker(lat: Double, lng: Double, id: Long? = null)

    suspend fun deleteMarkerByLatLng(lat: Double, lng: Double)

    suspend fun deleteMarkerById(id: Long)

}