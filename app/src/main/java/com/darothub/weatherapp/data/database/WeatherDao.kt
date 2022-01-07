package com.darothub.weatherapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.darothub.weatherapp.model.EasyWeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Insert(onConflict = REPLACE)
    suspend fun saveWeatherResponse(weatherResponse: EasyWeatherResponse)
    @Query("SELECT * FROM easyweatherresponse WHERE name = :locationName")
    fun getWeatherForecast(locationName: String): Flow<EasyWeatherResponse>
}
