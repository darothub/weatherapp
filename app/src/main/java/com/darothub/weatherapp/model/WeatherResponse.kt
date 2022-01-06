package com.darothub.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.darothub.weatherapp.data.database.Climate

@Entity
data class WeatherResponse(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    val current: Climate,
    val hourly: List<Climate>,
    val daily: List<Climate>
)

data class Current <T>(
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: T,
    val feelsLike: T,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val clouds: Long,
    val visibility: Long? = null,
    val windSpeed: Double,
    val windDeg: Long,
    val weather: List<Weather>,
    val windGust: Double? = null
)

data class Weather(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)
data class Temp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)
