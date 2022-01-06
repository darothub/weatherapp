package com.darothub.weatherapp.helper

import java.text.SimpleDateFormat
import java.util.*

fun convertKelvinToCelsius(temp: Double): Double {
    return "%.2f".format(temp - 273.15).toDouble()
}

fun convertLongToTime(longTime: Long): String {
    val dt = Date(longTime)
    val sdf = SimpleDateFormat("hh:mm a", Locale.US)
    return sdf.format(dt)
}
