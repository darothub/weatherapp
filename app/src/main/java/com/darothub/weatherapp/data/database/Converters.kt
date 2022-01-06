package com.darothub.weatherapp.data.database

import androidx.room.TypeConverter
import com.darothub.weatherapp.model.Temp
import com.darothub.weatherapp.model.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class Converters {
    val gson = Gson()
    @TypeConverter
    fun fromClimate(climate: Climate): String {
        return gson.toJson(climate)
    }
    @TypeConverter
    fun toClimateString(json: String): Climate {
        val listType: Type = object :
            TypeToken<Climate>() {}.type
        return gson.fromJson(json, listType)
    }
    @TypeConverter
    fun fromClimate(weather: List<Climate>): String {
        return gson.toJson(weather)
    }
    @TypeConverter
    fun toListClimateString(json: String): List<Climate> {
        val listType: Type = object :
            TypeToken<List<Climate>?>() {}.type
        return gson.fromJson(json, listType)
    }
    @TypeConverter
    fun fromWeather(weathers: List<Weather>): String {
        return gson.toJson(weathers)
    }
    @TypeConverter
    fun toListWeatherString(json: String): List<Weather> {
        val listType: Type = object :
            TypeToken<List<Weather>?>() {}.type
        return gson.fromJson(json, listType)
    }
    @TypeConverter
    fun fromTemp(temp: Temp): String {
        return gson.toJson(temp)
    }
    @TypeConverter
    fun toTempString(json: String): Temp {
        val listType: Type = object :
            TypeToken<Temp?>() {}.type
        return gson.fromJson(json, listType)
    }
}
