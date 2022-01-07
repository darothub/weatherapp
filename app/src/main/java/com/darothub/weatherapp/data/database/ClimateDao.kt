package com.darothub.weatherapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.darothub.weatherapp.model.EasyWeatherResponse
import com.darothub.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface ClimateDao {
//    @Insert(onConflict = IGNORE)
//    suspend fun insertAll(vararg weather: Climate)

    @Insert(onConflict = IGNORE)
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponse)
    @Insert(onConflict = IGNORE)
    suspend fun saveWeatherResponse(weatherResponse: EasyWeatherResponse)

    @Query("SELECT * FROM weatherresponse WHERE lat =:lat AND lon = :lon")
    fun getAll(lat: String, lon: String): LiveData<WeatherResponse>
    @Query("SELECT * FROM easyweatherresponse WHERE name = :locationName")
    fun getWeatherForecast(locationName: String): Flow<EasyWeatherResponse>
    @Query("SELECT * FROM weatherresponse WHERE lat LIKE :lat AND lon LIKE :lon")
    suspend fun getAllList(lat: String, lon: String): WeatherResponse
}
