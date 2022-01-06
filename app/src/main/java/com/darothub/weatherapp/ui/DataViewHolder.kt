package com.darothub.weatherapp.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.darothub.weatherapp.data.database.Climate
import com.darothub.weatherapp.databinding.ForecastItemLayoutBinding
import com.darothub.weatherapp.helper.getLongDate

class DataViewHolder(private val binding: ForecastItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(climate: Climate) {
        binding.apply {
            temp.textSize = 14f
            temp.text = getLongDate(climate.dt)
            description.text = climate.weather[0].description
            wind.text = "Wind: ${climate.windSpeed}m/s"
            pressure.text = "Pressure: ${climate.pressure}hPa"
            humidity.text = "Humidity: ${climate.humidity}%"
            sunrise.visibility = View.GONE
            sunset.visibility = View.GONE
        }
    }
}
