package com.darothub.weatherapp.ui

interface UiStateListener {
    fun <T> onSuccess(data: T)
    fun onError(error: String?)
    fun onNetworkError()
    fun loading()
}
