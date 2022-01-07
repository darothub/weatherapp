package com.darothub.weatherapp.data.api

import com.darothub.weatherapp.model.EasyWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query
const val baseUrl2 = "https://api.weatherapi.com/v1/"
interface ApiService {

    @GET("forecast.json")
    suspend fun getEasyWeatherForeCast(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("days") days: Int,
    ): EasyWeatherResponse
}
