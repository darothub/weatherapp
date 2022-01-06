package com.darothub.weatherapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.darothub.weatherapp.domain.repository.DataRepository

class WeatherViewModelFactory(private val dataRepository: DataRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(dataRepository) as T
    }
}
