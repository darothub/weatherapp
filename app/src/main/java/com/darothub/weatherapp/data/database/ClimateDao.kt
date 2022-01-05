package com.darothub.weatherapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ClimateDao {
    @Insert
    fun insertAll(vararg climate: Climate)

    @Insert
    fun insertClimate(climate: Climate)

    @Query("SELECT * FROM climate ORDER by dt ASC")
    fun getAll(): LiveData<List<Climate>>
}
