package com.darothub.weatherapp.data.database

import com.darothub.weatherapp.model.Weather
import com.google.gson.annotations.SerializedName

data class Climate(
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: Any,
    val pressure: Long,
    val humidity: Long,
    @SerializedName("dew_point")
    val dewPoint: Double,
    val clouds: Long,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    val weather: List<Weather>,
)
data class Climate2<T>(
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: T,
    val pressure: Long,
    val humidity: Long,
    @SerializedName("dew_point")
    val dewPoint: Double,
    val clouds: Long,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    val weather: List<Weather>,
)
