package com.darothub.weatherapp.ui

import androidx.recyclerview.widget.RecyclerView
import com.darothub.weatherapp.data.database.Climate
import com.darothub.weatherapp.databinding.ForecastItemLayoutBinding

class DataViewHolder(private val binding: ForecastItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(climate: Climate) {
        binding.apply {
            temp.text = climate.temp.toString()
            description.text = climate.weather[0].description
            wind.text = climate.windSpeed.toString()
            pressure.text = climate.pressure.toString()
            humidity.text = climate.humidity.toString()
            sunrise.text = climate.sunrise.toString()
            sunset.text = climate.sunset.toString()
        }
    }
}
