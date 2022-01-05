package com.darothub.weatherapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.darothub.weatherapp.model.Weather
import com.google.gson.annotations.SerializedName

@Entity
data class Climate(
    @PrimaryKey
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: Double,
    val pressure: Long,
    val humidity: Long,
    @ColumnInfo(name = "dew_point")
    val dewPoint: Double,
    val clouds: Long,
    @ColumnInfo(name = "wind_speed")
    @SerializedName("wind_speed")
    val windSpeed: Double,
    val weather: List<Weather>,
)
