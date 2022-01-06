package com.darothub.weatherapp.ui

import android.text.SpannableString
import android.text.Spanned
import android.text.style.SuperscriptSpan
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.darothub.weatherapp.data.database.Climate
import com.darothub.weatherapp.databinding.ForecastItemLayoutBinding
import com.darothub.weatherapp.helper.convertKelvinToCelsius
import com.darothub.weatherapp.model.Temp

class DataViewHolder(private val binding: ForecastItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(climate: Climate) {
        var tempInCelsius = when (climate.temp) {
            is Double -> convertKelvinToCelsius(climate.temp)
            is Temp -> convertKelvinToCelsius(climate.temp.min)
            else -> 0.0
        }

        val s = SpannableString("$tempInCelsius" + "oC")
        s.setSpan(SuperscriptSpan(), 5, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.apply {
            temp.text = s
            description.text = climate.weather[0].description
            wind.text = "Wind: ${climate.windSpeed}m/s"
            pressure.text = "Pressure: ${climate.pressure}hPa"
            humidity.text = "Humidity: ${climate.humidity}%"
            sunrise.visibility = View.GONE
            sunset.visibility = View.GONE
        }
    }
}
