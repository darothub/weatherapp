package com.darothub.weatherapp.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class EasyWeatherResponse(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @Embedded
    val location: Location,
    val current: Current,
    val forecast: Forecast
)

data class Current(
    val lastUpdatedEpoch: Long,
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("temp_c")
    val tempC: Double,
    val condition: Condition,
    @SerializedName("wind_mph")
    val windMph: Double,
    @SerializedName("pressure_in")
    val pressureIn: Double,
    val humidity: Long,
)

data class Condition(
    val text: String,
    val icon: String,
    val code: Long
)

data class Forecast(
    val forecastday: List<Forecastday>
)

data class Forecastday(
    val date: String,
    val dateEpoch: Long,
    val day: Day,
    val astro: Astro,
    val hour: List<Hour>
)

data class Astro(
    val sunrise: String,
    val sunset: String,
)

data class Day(
    @SerializedName("avgtemp_c")
    val avgtempC: Double,
    val condition: Condition,
)

data class Hour(
    @SerializedName("time_epoch")
    val timeEpoch: Long,
    @SerializedName("temp_c")
    val tempC: Double,
    val condition: Condition,
    @SerializedName("wind_mph")
    val windMph: Double,
    @SerializedName("pressure_in")
    val pressureIn: Double,
    val humidity: Long,
) : Serializable

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tzID: String,
    val localtimeEpoch: Long,
    val localtime: String
)
