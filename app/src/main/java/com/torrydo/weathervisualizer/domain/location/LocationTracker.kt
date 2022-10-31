package com.torrydo.weathervisualizer.domain.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationTracker {

    suspend fun getCurrentLocation(): Location?

}