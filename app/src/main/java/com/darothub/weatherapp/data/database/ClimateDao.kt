package com.darothub.weatherapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.darothub.weatherapp.model.WeatherResponse

@Dao
interface ClimateDao {
//    @Insert(onConflict = IGNORE)
//    suspend fun insertAll(vararg weather: Climate)

    @Insert(onConflict = IGNORE)
    suspend fun insertWeatherResponse(weatherResponse: WeatherResponse)

    @Query("SELECT * FROM weatherresponse WHERE lat LIKE :lat AND lon LIKE :lon")
    fun getAll(lat: String, lon: String): LiveData<WeatherResponse>

    @Query("SELECT * FROM weatherresponse WHERE lat LIKE :lat AND lon LIKE :lon")
    suspend fun getAllList(lat: String, lon: String): WeatherResponse
}
