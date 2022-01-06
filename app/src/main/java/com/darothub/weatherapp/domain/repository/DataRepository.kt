package com.darothub.weatherapp.domain.repository

import com.darothub.weatherapp.model.WeatherResponse

class DataRepository(
    private val localDataManager: LocalDataManager,
    private val remoteDataManager: RemoteDataManager
) {

    suspend fun getClimateForeCast(
        lat: String,
        lon: String,
        exclude: String,
        app_id: String
    ): WeatherResponse {
        val latQuery = "%${lat.replace(' ', '%')}%"
        val lonQuery = "%${lon.replace(' ', '%')}%"
        var data = localDataManager.getClimatesList(latQuery, lonQuery)
        if (data == null) {
            data = remoteDataManager.getWeatherDetails(lat, lon, exclude, app_id)
            localDataManager.saveClimate(data)
            return data
        }
        return data
    }

    suspend fun getClimateForecastWithDate(
        lat: String,
        lon: String,
        dt: String,
        exclude: String,
        app_id: String
    ): WeatherResponse {
        val latQuery = "%${lat.replace(' ', '%')}%"
        val lonQuery = "%${lon.replace(' ', '%')}%"
        var data = localDataManager.getClimatesList(latQuery, lonQuery)
        if (data == null) {
            data = remoteDataManager.getClimateForecast(lat, lon, dt, exclude, app_id)
            localDataManager.saveClimate(data)
            return data
        }
        return data
    }
}
