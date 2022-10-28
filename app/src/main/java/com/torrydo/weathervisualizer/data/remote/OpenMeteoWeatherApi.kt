package com.torrydo.weathervisualizer.data.remote

import com.torrydo.weathervisualizer.common.error.DefaultError
import com.torrydo.weathervisualizer.common.model.Resource
import com.torrydo.weathervisualizer.utils.Logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Suppress("SpellCheckingInspection")
class OpenMeteoWeatherApi {

    private val URL_SOURCE =
        "https://api.open-meteo.com/v1/forecast?&hourly=temperature_2m,relativehumidity_2m,windspeed_10m,weathercode,pressure_msl&timezone=Asia%2FBangkok&{latitude}&{longitude}"


    private val jsonFormat = Json { ignoreUnknownKeys = true }

    suspend fun getWeather(lat: Double, lon: Double): Resource<OpenMeteoDto> {

        val url = URL_SOURCE
            .replace("{latitude}", "latitude=$lat")
            .replace("{longitude}", "longitude=$lon")

        val client = HttpClient(CIO)
        val response: HttpResponse = client.get(url)
        client.close()

        return try {

            val openMeteoDto = jsonFormat.decodeFromString<OpenMeteoDto>(response.body())

            Resource.Success(openMeteoDto)
        } catch (e: Exception) {
            Resource.Error(DefaultError(e.stackTraceToString()))
        }

    }

}