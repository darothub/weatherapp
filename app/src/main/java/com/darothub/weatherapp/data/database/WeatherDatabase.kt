package com.darothub.weatherapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Database(
    entities = [Climate::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun climateDao(): ClimateDao

   companion object DbModule {
       private var instance: WeatherDatabase? = null
       @Synchronized
       fun getInstance(ctx: Context): WeatherDatabase {
           if(instance == null)
               instance = Room.databaseBuilder(ctx.applicationContext, WeatherDatabase::class.java,
                   "weatherdb")
                   .fallbackToDestructiveMigration()
                   .build()

           return instance!!

       }
    }
}