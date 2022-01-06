package com.darothub.weatherapp.ui

import android.util.Log
import androidx.lifecycle.*
import com.darothub.weatherapp.domain.repository.DataRepository
import com.darothub.weatherapp.helper.SingleLiveEvent
import com.darothub.weatherapp.model.WeatherResponse
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: DataRepository) : ViewModel() {
    private val _weatherLiveData = SingleLiveEvent<WeatherResponse>()
    var weatherLiveData: SingleLiveEvent<WeatherResponse> = _weatherLiveData

    suspend fun getClimates(
        lat: String?,
        lon: String?,
        exclude: String?,
        app_id: String?
    ) {
        repository.getLocalClimates(lat!!, lon!!).apply {
            viewModelScope.launch {
                try {
                    val response = repository.getRemoteClimates(lat, lon, exclude!!, app_id!!)
                    repository.saveClimate(response)
                    _weatherLiveData.postValue(response)
                    Log.d("Viewmodel", response.daily.toString())
                } catch (e: Exception) {
                    Log.d("Viewmodel", e.message.toString())
                }
            }
        }
    }

    suspend fun getClimateForecast(
        lat: String?,
        lon: String?,
        dt: String?,
        exclude: String?,
        app_id: String?
    ) {
        repository.getLocalClimateForeCast(lat!!, lon!!).apply {
            viewModelScope.launch {
                try {
                    val response = repository.getRemoteClimateForecast(lat, lon, dt!!, exclude!!, app_id!!)
                    repository.saveClimate(response)
                    _weatherLiveData.postValue(response)
                    Log.d("ViewmodelForecast", response.daily.toString())
                } catch (e: Exception) {
                    Log.d("Viewmodel", e.message.toString())
                }
            }
        }
    }
}
