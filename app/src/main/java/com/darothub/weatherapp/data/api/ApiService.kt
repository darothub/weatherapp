package com.darothub.weatherapp.data.api

import com.darothub.weatherapp.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
const val baseUrl = "https://api.openweathermap.org/data/2.5/"
interface ApiService {
    @GET("onecall?")
    suspend fun getWeatherService(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String,
        @Query("appid") app_id: String
    ): WeatherResponse
}
