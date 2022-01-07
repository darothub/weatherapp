package com.darothub.weatherapp.domain.repository

import com.darothub.weatherapp.model.EasyWeatherResponse

class DataRepository(
    private val localDataManager: LocalDataManager,
    private val remoteDataManager: RemoteDataManager
) {

    fun getLocalForecast(q: String) = localDataManager.getEasyWeatherForecast(q)
    suspend fun getEasyForeCast(
        key: String,
        q: String,
        days: Int,
    ): EasyWeatherResponse {
        val remoteData = remoteDataManager.getEasyForecast(key, q, days)
        localDataManager.saveEasy(remoteData)
        return remoteData
    }
}
