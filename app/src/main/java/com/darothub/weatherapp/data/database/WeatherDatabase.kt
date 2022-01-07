package com.darothub.weatherapp.data.database

import android.content.Context
import androidx.room.*
import com.darothub.weatherapp.model.EasyWeatherResponse
import com.darothub.weatherapp.model.WeatherResponse

@Database(
    entities = [EasyWeatherResponse::class, WeatherResponse::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun climateDao(): ClimateDao

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            WeatherDatabase::class.java, "weatherdb"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
