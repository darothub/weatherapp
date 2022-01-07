package com.darothub.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.araujo.jordan.excuseme.ExcuseMe
import com.darothub.weatherapp.MainApplication
import com.darothub.weatherapp.R
import com.darothub.weatherapp.databinding.ActivityMainBinding
import com.darothub.weatherapp.helper.convertLongToTime
import com.darothub.weatherapp.helper.convertTempToScientificReading
import com.darothub.weatherapp.helper.setTextsColorToWhite
import com.darothub.weatherapp.model.*
import com.darotpeacedude.eivom.utils.viewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import java.util.*

const val ARG_OBJECT = "object"
const val API_KEY = "358c550eaa931d17bbe2602e25ec8d72"
const val API_KEY2 = "a3dcb83d3dca466c9a4155723220501"

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    lateinit var adapter: ViewPagerAdapter
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    val viewModel by viewModels<WeatherViewModel> { WeatherViewModelFactory(MainApplication.getRepository()) }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        ExcuseMe.couldYouGive(this).permissionFor(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
        ) {
            if (it.granted.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                mFusedLocationClient!!.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {},
                    Looper.getMainLooper()
                )
                getLastLocation()
            } else {
                lifecycleScope.launch {
                    ExcuseMe.couldYouGive(this@MainActivity).gently(
                        "Permission Request",
                        "To be able to get your current location you need to accept this request"
                    ).permissionFor(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchMenu = menu?.findItem(R.id.menu_search)
        val searchView = searchMenu?.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            getWeatherData(query)
        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return true
    }
    private fun getAddressFromLatLng(lat: Double, long: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val result = try {
            val addressList = geocoder.getFromLocation(lat, long, 1)
            if (addressList != null) {
                val singleAddress = addressList[0]
                val locality = singleAddress.locality.replace(" ", "")
                Log.d("ADDRESS", "$singleAddress $locality")
                singleAddress.locality
            } else {
                "null"
            }
        } catch (e: Exception) {
            e.message
        }
        return result as String
    }
    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        mFusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            Log.d("Main", "lat $location")
            if (location != null) {
                val locality = getAddressFromLatLng(location.latitude, location.longitude)
                getWeatherData(locality)
            }
        }
    }

    private fun getWeatherData(locality: String) {
        viewModel.getLocalEasyForecast(locality).observe(this) { easyWeather ->

            if (easyWeather == null) {
                viewModel.getEasyForecast(API_KEY2, locality)
            }
        }
        viewModel.weatherLiveData.observe(this) { wrState ->
            if (wrState != null) observeRequest(wrState)
        }
    }

    private fun observeRequest(wrState: UIState) {
        when (wrState) {
            is UIState.Success<*> -> {
                binding.main.root.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                val wr = wrState.data as EasyWeatherResponse
                var tempInCelsius = wr.current.tempC
                val s = convertTempToScientificReading(tempInCelsius)
                binding.main.apply {
                    setTextsColorToWhite(temp)
                    setTextsColorToWhite(description)
                    setTextsColorToWhite(wind)
                    setTextsColorToWhite(pressure)
                    setTextsColorToWhite(humidity)
                    setTextsColorToWhite(sunrise)
                    setTextsColorToWhite(sunset)
                    setTextsColorToWhite(updateDateTv)
                }

                binding.main.apply {
                    val desc = wr.current.condition.text
                    root.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.purple_500
                        )
                    )
                    temp.text = s
                    description.text = desc
                    val icon = "https:" + wr.current.condition.icon
                    Log.d("ICON", icon)
                    binding.main.weatherImage.load(icon)
                    wind.text = "Wind: ${wr.current.windMph}m/s"
                    pressure.text = "Pressure: ${wr.current.pressureIn}hPa"
                    humidity.text = "Humidity: ${wr.current.humidity}%"
                    sunrise.text =
                        "Sunrise: ${wr.forecast.forecastday[0].astro.sunrise}"
                    sunset.text = "Sunset: ${wr.forecast.forecastday[0].astro.sunset}"
                    val lastUpdated = convertLongToTime(wr.current.lastUpdatedEpoch)
                    updateDateTv.text = "Last update: $lastUpdated"
                }

                adapter = ViewPagerAdapter(this@MainActivity, 3) { position ->
                    val listOfDays = wr.forecast.forecastday
                    when (position) {
                        0 -> {
                            TabFragment.newInstance(listOfDays[0].hour)
                        }
                        1 -> {
                            TabFragment.newInstance(listOfDays[1].hour)
                        }
                        else -> {
                            TabFragment.newInstance(listOfDays[2].hour)
                        }
                    }
                }

                binding.vp.adapter = adapter
                TabLayoutMediator(binding.mainTabLayout, binding.vp) { tab, position ->
                    when (position) {
                        0 -> tab.text = "Today"
                        1 -> tab.text = "Tomorrow"
                        2 -> tab.text = "Later"
                    }
                }.attach()
            }
            is UIState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.main.root.visibility = View.GONE
            }
        }
    }
}
