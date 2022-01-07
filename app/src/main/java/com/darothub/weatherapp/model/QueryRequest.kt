package com.darothub.weatherapp.model

import java.io.Serializable

data class QueryRequest(
    val lat: String? = "",
    val lon: String? = "",
    val dt: String? = "",
    val exclude: String? = "",
    val appid: String? = ""
) : Serializable
