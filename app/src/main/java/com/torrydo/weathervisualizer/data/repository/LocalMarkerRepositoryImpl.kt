package com.torrydo.weathervisualizer.data.repository

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.torrydo.weathervisualizer.MarkerDatabase
import com.torrydo.weathervisualizer.domain.repository.LocalMarkerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import maps.markerdb.MarkerEntity

class LocalMarkerRepositoryImpl(
    db: MarkerDatabase
) : LocalMarkerRepository {

    private val queries = db.markerEntityQueries

    private suspend fun <T> io(content: CoroutineScope.() -> T): T {
        return withContext(Dispatchers.IO, content)
    }

    override suspend fun getMarkerById(id: Long): MarkerEntity? {
        return io {
            queries.getMarkerById(id).executeAsOneOrNull()
        }
    }

    override suspend fun getMarkerByLatLng(lat: Double, lng: Double): MarkerEntity? {
        return io {
            queries.getMarkerByLatLng(lat, lng).executeAsOneOrNull()
        }
    }

    override fun getAllMarkers(): Flow<List<MarkerEntity>> {
        return queries.getAllMarkers().asFlow().mapToList()
    }

    override suspend fun insertMarker(lat: Double, lng: Double, id: Long?) = io {
        queries.insertMarker(id, lat, lng)
    }

    override suspend fun deleteMarkerByLatLng(lat: Double, lng: Double) = io {
        queries.deleteMarkerByLatLng(lat, lng)
    }


    override suspend fun deleteMarkerById(id: Long) = io {
        queries.deleteMarkerById(id)
    }


}