package com.darothub.weatherapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Climate::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun climateDao(): ClimateDao
}