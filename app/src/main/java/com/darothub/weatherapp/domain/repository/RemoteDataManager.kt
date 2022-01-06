package com.darothub.weatherapp.domain.repository

import com.darothub.weatherapp.data.api.ApiService
import com.darothub.weatherapp.model.WeatherResponse

class RemoteDataManager(private val apiService: ApiService) {

    suspend fun getWeatherDetails(
        lat: String,
        lon: String,
        exclude: String,
        app_id: String
    ): WeatherResponse {
        return apiService.getWeatherService(lat, lon, exclude, app_id)
    }
}