package com.darothub.weatherapp.data.api

import com.darothub.weatherapp.model.EasyWeatherResponse
import com.darothub.weatherapp.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
const val baseUrl = "https://api.openweathermap.org/data/2.5/"
const val baseUrl2 = "https://api.weatherapi.com/v1/"
interface ApiService {
    @GET("onecall")
    suspend fun getWeatherService(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String,
        @Query("appid") app_id: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
    @GET("onecall")
    suspend fun getWeatherForeCast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("dt") dt: String,
        @Query("exclude") exclude: String,
        @Query("appid") app_id: String
    ): WeatherResponse

    @GET("forecast.json")
    suspend fun getEasyWeatherForeCast(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: Int,
    ): EasyWeatherResponse
}
