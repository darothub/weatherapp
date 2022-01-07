package com.darothub.weatherapp.data.database

import androidx.room.TypeConverter
import com.darothub.weatherapp.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class Converters {
    val gson = Gson()

    @TypeConverter
    fun fromCurrent(current: Current): String {
        return gson.toJson(current)
    }
    @TypeConverter
    fun toCurrent(json: String): Current {
        val current: Type = object :
            TypeToken<Current?>() {}.type
        return gson.fromJson(json, current)
    }
    @TypeConverter
    fun fromCondition(condition: Condition): String {
        return gson.toJson(condition)
    }
    @TypeConverter
    fun toCondition(json: String): Condition {
        val condition: Type = object :
            TypeToken<Condition?>() {}.type
        return gson.fromJson(json, condition)
    }
    @TypeConverter
    fun fromListForecastDay(forecastdays: List<Forecastday>): String {
        return gson.toJson(forecastdays)
    }
    @TypeConverter
    fun toListForecastDay(json: String): List<Forecastday> {
        val forecastdays: Type = object :
            TypeToken<List<Forecastday>?>() {}.type
        return gson.fromJson(json, forecastdays)
    }
    @TypeConverter
    fun fromListHour(hours: List<Hour>): String {
        return gson.toJson(hours)
    }
    @TypeConverter
    fun toListHour(json: String): List<Hour> {
        val hours: Type = object :
            TypeToken<List<Hour>?>() {}.type
        return gson.fromJson(json, hours)
    }

    @TypeConverter
    fun fromAstro(astro: Astro): String {
        return gson.toJson(astro)
    }
    @TypeConverter
    fun toAstro(json: String): Astro {
        val astro: Type = object :
            TypeToken<Astro?>() {}.type
        return gson.fromJson(json, astro)
    }
    @TypeConverter
    fun fromForecast(forecast: Forecast): String {
        return gson.toJson(forecast)
    }
    @TypeConverter
    fun toForecast(json: String): Forecast {
        val forecast: Type = object :
            TypeToken<Forecast?>() {}.type
        return gson.fromJson(json, forecast)
    }
}
