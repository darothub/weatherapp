package com.darothub.weatherapp.domain.repository

import com.darothub.weatherapp.data.database.ClimateDao
import com.darothub.weatherapp.model.WeatherResponse

class LocalDataManager(private val climateDao: ClimateDao) {

    suspend fun saveClimate(weather: WeatherResponse) {
        climateDao.insertWeatherResponse(weather)
    }
    fun getClimates(lat: String, lon: String) = climateDao.getAll(lat, lon)

    suspend fun getClimateForecast(lat: String, lon: String) = climateDao.getAllList(lat, lon)
    suspend fun getClimatesList(lat: String, lon: String) = climateDao.getAllList(lat, lon)
}
