package com.darothub.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.darothub.weatherapp.data.database.Climate
import java.io.Serializable

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
) : Serializable

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
