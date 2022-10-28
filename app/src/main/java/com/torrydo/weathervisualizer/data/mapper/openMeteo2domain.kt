package com.torrydo.weathervisualizer.data.mapper

import com.torrydo.weathervisualizer.data.remote.OpenMeteoDto
import com.torrydo.weathervisualizer.data.remote.OpenMeteoHourlyDto
import com.torrydo.weathervisualizer.domain.weather.WeatherData
import com.torrydo.weathervisualizer.domain.weather.WeatherInfo
import com.torrydo.weathervisualizer.domain.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)

fun OpenMeteoDto.toWeatherInfo(): WeatherInfo {
    val weatherDataMap = this.hourly.toWeatherDataMap()
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[0]?.find {
        val hour = if(now.minute < 30) now.hour else now.hour + 1
        it.time.hour == hour
    }
    return WeatherInfo(
        weatherPerDay = weatherDataMap,
        currentWeather = currentWeatherData
    )
}

fun OpenMeteoHourlyDto.toWeatherDataMap(): Map<Int, List<WeatherData>> {

    return this.time.mapIndexed { index, time ->
        val localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME)
        val temperature = temperature_2m[index]
        val humidity = relativehumidity_2m[index]
        val windSpeed = windspeed_10m[index]
        val weatherType = WeatherType.fromWMO(weatherCode[index])
        val pressure = pressure_msl[index]

        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = localDateTime,
                temperature = temperature,
                humidity = humidity,
                windSpeed = windSpeed,
                weatherType = weatherType,
                pressureMsl = pressure
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues {
        it.value.map { it.data }
    }

}