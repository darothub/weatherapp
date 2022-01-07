package com.darothub.weatherapp.ui

import androidx.lifecycle.*
import com.darothub.weatherapp.domain.repository.DataRepository
import com.darothub.weatherapp.helper.SingleLiveEvent
import com.darothub.weatherapp.model.UIState
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: DataRepository) : ViewModel() {
    private val _weatherLiveData = SingleLiveEvent<UIState>()
    var weatherLiveData: SingleLiveEvent<UIState> = _weatherLiveData

    fun getClimates(
        lat: String?,
        lon: String?,
        exclude: String?,
        app_id: String?
    ) {
        _weatherLiveData.postValue(UIState.Loading)
        viewModelScope.launch {
            try {
                val response = repository.getClimateForeCast(lat.toString(), lon.toString(), exclude.toString(), app_id.toString())
                _weatherLiveData.postValue(UIState.Success(response))
            } catch (e: Exception) {
                _weatherLiveData.postValue(UIState.Error(e))
            }
        }
    }
    fun getLocalEasyForecast(q: String) = repository.getLocalForecast(q).asLiveData()
    fun getEasyForecast(
        key: String,
        q: String,
        days: Int = 3,
    ) {
        _weatherLiveData.postValue(UIState.Loading)
        viewModelScope.launch {
            try {
                val response = repository.getEasyForeCast(key, q, days)
                _weatherLiveData.postValue(UIState.Success(response))
            } catch (e: Exception) {
                _weatherLiveData.postValue(UIState.Error(e))
            }
        }
    }

    fun getClimateForecast(
        lat: String?,
        lon: String?,
        dt: String?,
        exclude: String?,
        app_id: String?
    ) {

        viewModelScope.launch {
            try {
                val response = repository.getClimateForecastWithDate(lat.toString(), lon.toString(), dt.toString(), exclude.toString(), app_id.toString())
                _weatherLiveData.postValue(UIState.Success(response))
            } catch (e: Exception) {
                _weatherLiveData.postValue(UIState.Error(e))
            }
        }
    }
}
