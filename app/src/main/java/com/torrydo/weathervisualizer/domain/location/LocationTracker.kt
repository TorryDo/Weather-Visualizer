package com.torrydo.weathervisualizer.domain.location

import android.location.Location

interface LocationTracker {

    suspend fun getCurrentLocation(): Location?

}