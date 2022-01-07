package com.darothub.weatherapp.ui

import androidx.lifecycle.*
import com.darothub.weatherapp.domain.repository.DataRepository
import com.darothub.weatherapp.helper.SingleLiveEvent
import com.darothub.weatherapp.model.UIState
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: DataRepository) : ViewModel() {
    private val _weatherLiveData = SingleLiveEvent<UIState>()
    var weatherLiveData: SingleLiveEvent<UIState> = _weatherLiveData

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
}
