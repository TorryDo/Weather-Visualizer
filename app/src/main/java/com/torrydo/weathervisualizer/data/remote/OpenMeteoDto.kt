@file:Suppress("SpellCheckingInspection")

package com.torrydo.weathervisualizer.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class OpenMeteoDto(
    @SerialName("hourly_units")
    val units: OpenMeteoUnitsDto,

    @SerialName("hourly")
    val hourly: OpenMeteoHourlyDto
)

@Serializable
data class OpenMeteoUnitsDto(
    @SerialName("time")
    val time: String = "None",

    @SerialName("temperature_2m")
    val temperature_2m: String = "None",

    @SerialName("relativehumidity_2m")
    val relativehumidity_2m: String = "None",

    @SerialName("windspeed_10m")
    val windspeed_10m: String = "None",

    @SerialName("weathercode")
    val weatherCode: String = "None",

    @SerialName("pressure_msl")
    val pressure_msl: String = "None",
)

@Serializable
data class OpenMeteoHourlyDto(
    @SerialName("time")
    val time: List<String> = emptyList(),

    @SerialName("temperature_2m")
    val temperature_2m: List<Double> = emptyList(),

    @SerialName("relativehumidity_2m")
    val relativehumidity_2m: List<Int> = emptyList(),

    @SerialName("windspeed_10m")
    val windspeed_10m: List<Double> = emptyList(),

    @SerialName("weathercode")
    val weatherCode: List<Int> = emptyList(),

    @SerialName("pressure_msl")
    val pressure_msl: List<Double> = emptyList()
)