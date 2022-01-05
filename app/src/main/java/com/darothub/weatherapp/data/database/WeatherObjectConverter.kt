package com.darothub.weatherapp.data.database

import androidx.room.TypeConverter
import com.darothub.weatherapp.model.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class WeatherObjectConverter {
    val gson = Gson()
    @TypeConverter
    fun fromWeather(weather: Weather): String {
        return gson.toJson(weather)
    }
    @TypeConverter
    fun fromJsonString(json: String): List<Weather> {
        if (json == null) {
            return Collections.emptyList()
        }
        val listType: Type = object :
            TypeToken<List<List<Weather>?>?>() {}.type
        return gson.fromJson(json, listType)
    }
}
