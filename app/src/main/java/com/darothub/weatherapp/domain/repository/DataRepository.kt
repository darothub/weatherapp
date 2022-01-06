package com.darothub.weatherapp.domain.repository

import androidx.lifecycle.LiveData
import com.darothub.weatherapp.model.WeatherResponse

class DataRepository(
    private val localDataManager: LocalDataManager,
    private val remoteDataManager: RemoteDataManager
) {
    suspend fun saveClimate(weather: WeatherResponse) {
        localDataManager.saveClimate(weather)
    }

    fun getLocalClimates(lat: String, lon: String): LiveData<WeatherResponse> {
        val latQuery = "%${lat.replace(' ', '%')}%"
        val lonQuery = "%${lon.replace(' ', '%')}%"
        return localDataManager.getClimates(latQuery, lonQuery)
    }

    suspend fun getLocalClimatesList(lat: String, lon: String): WeatherResponse {
        val latQuery = "%${lat.replace(' ', '%')}%"
        val lonQuery = "%${lon.replace(' ', '%')}%"
        return localDataManager.getClimatesList(latQuery, lonQuery)
    }

    suspend fun getRemoteClimates(
        lat: String,
        lon: String,
        exclude: String,
        app_id: String
    ): WeatherResponse {
        return remoteDataManager.getWeatherDetails(lat, lon, exclude, app_id)
    }
}
