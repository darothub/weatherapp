package com.darothub.weatherapp.domain.repository

import com.darothub.weatherapp.data.api.ApiService
import com.darothub.weatherapp.model.EasyWeatherResponse

class RemoteDataManager(private val apiService: ApiService) {
    suspend fun getEasyForecast(
        key: String,
        q: String,
        days: Int,
    ): EasyWeatherResponse {
        return apiService.getEasyWeatherForeCast(key, q, days)
    }
}
