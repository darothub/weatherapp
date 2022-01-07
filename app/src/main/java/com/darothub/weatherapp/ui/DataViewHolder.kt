package com.darothub.weatherapp.ui

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.darothub.weatherapp.R
import com.darothub.weatherapp.databinding.ForecastItemLayoutBinding
import com.darothub.weatherapp.helper.convertEpochTimeToStringFormat
import com.darothub.weatherapp.helper.convertTempToScientificReading
import com.darothub.weatherapp.model.Hour

class DataViewHolder(private val binding: ForecastItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(hour: Hour) {
        binding.apply {
            temp.textSize = 14f
            val s = convertTempToScientificReading(hour.tempC)
            updateDateTv.text = s
            temp.text = convertEpochTimeToStringFormat(hour.timeEpoch)
            description.text = hour.condition.text
            wind.text = "Wind: ${hour.windMph}m/s"
            pressure.text = "Pressure: ${hour.pressureIn}hPa"
            humidity.text = "Humidity: ${hour.humidity}%"
            sunrise.visibility = View.GONE
            sunset.visibility = View.GONE
            val icon = "https:" + hour.condition.icon
            weatherImage.load(icon)
            weatherImage.setColorFilter(ContextCompat.getColor(weatherImage.context, R.color.black), android.graphics.PorterDuff.Mode.MULTIPLY)
        }
    }
}
