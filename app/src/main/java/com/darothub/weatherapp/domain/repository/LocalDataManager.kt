package com.darothub.weatherapp.domain.repository

import com.darothub.weatherapp.data.database.WeatherDao
import com.darothub.weatherapp.model.EasyWeatherResponse

class LocalDataManager(private val climateDao: WeatherDao) {

    suspend fun saveEasy(weather: EasyWeatherResponse) {
        climateDao.saveWeatherResponse(weather)
    }
    fun getEasyWeatherForecast(locationName: String) = climateDao.getWeatherForecast(locationName)
}
