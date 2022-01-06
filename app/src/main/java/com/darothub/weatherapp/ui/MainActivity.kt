package com.darothub.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.style.SuperscriptSpan
import android.util.Log
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.araujo.jordan.excuseme.ExcuseMe
import com.darothub.weatherapp.MainApplication
import com.darothub.weatherapp.R
import com.darothub.weatherapp.databinding.ActivityMainBinding
import com.darothub.weatherapp.helper.convertKelvinToCelsius
import com.darothub.weatherapp.helper.convertLongToTime
import com.darothub.weatherapp.model.QueryRequest
import com.darothub.weatherapp.model.Temp
import com.darotpeacedude.eivom.utils.viewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

const val ARG_OBJECT = "object"
const val API_KEY = "358c550eaa931d17bbe2602e25ec8d72"

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
            Log.d("Query", query)
            val (lat, lng) = getLatLngFromAddress(query.toString())
            CoroutineScope(Dispatchers.Main).launch {
                getLocationWeatherDetails(lat, lng)
            }
        }
        return true
    }

    @SuppressLint("SetTextI18n")
    private suspend fun getLocationWeatherDetails(
        lat: String,
        lng: String
    ) {
        viewModel.getClimates(lat, lng, "minutely", API_KEY)
        withContext(Dispatchers.Main) {
            viewModel.weatherLiveData.observe(
                this@MainActivity,
                { wr ->
                    var tempInCelsius = when (wr.current.temp) {
                        is Double -> convertKelvinToCelsius(wr.current.temp)
                        is Temp -> convertKelvinToCelsius(wr.current.temp.min)
                        else -> 0.0
                    }

                    val s = SpannableString("$tempInCelsius" + "oC")
                    s.setSpan(SuperscriptSpan(), 5, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                    binding.main.apply {
                        root.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.purple_500))
                        temp.text = s
                        description.text = wr.current.weather[0].description
                        wind.text = "Wind: ${wr.current.windSpeed}m/s"
                        pressure.text = "Pressure: ${wr.current.pressure}hPa"
                        humidity.text = "Humidity: ${wr.current.humidity}%"
                        sunrise.text = "Sunrise: ${wr.current.sunrise?.let { convertLongToTime(it) }}"
                        sunset.text = "Sunset: ${wr.current.sunset?.let { convertLongToTime(it) }}"
                    }

                    adapter = ViewPagerAdapter(this@MainActivity, 3) { position ->
                        when (position) {
                            1 -> {
                                val queryRequest = QueryRequest(lat, lng, wr.daily[1].dt.toString(), "minutely", API_KEY)
                                TabFragment.newInstance(queryRequest)
                            }
                            0 -> {
                                val queryRequest = QueryRequest(lat, lng, wr.daily[0].dt.toString(), "minutely", API_KEY)
                                TabFragment.newInstance(queryRequest)
                            }
                            else -> {
                                val queryRequest = QueryRequest(lat, lng, wr.daily[2].dt.toString(), "minutely", API_KEY)
                                TabFragment.newInstance(queryRequest)
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
                    viewModel.weatherLiveData.removeObservers(this@MainActivity)
                }
            )
        }
    }

    override fun onQueryTextChange(p0: String?): Boolean {
//        Log.d("Main2", p0.toString())
        return true
    }

    private fun getLatLngFromAddress(address: String): Pair<String, String> {
        val geocoder = Geocoder(this, Locale.getDefault())
        val result = try {
            val addressList = geocoder.getFromLocationName(address, 1)
            if (addressList != null) {
                val singleAddress = addressList[0]
                val latitude = "%.2f".format(singleAddress.latitude)
                val longitude = "%.2f".format(singleAddress.longitude)
                Pair(latitude, longitude)
            } else {
                Pair("", "")
            }
        } catch (e: Exception) {
            Pair("", "")
        }
        return result
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        mFusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            Log.d("Main", "lat $location")
            if (location != null) {
                val lat = location.latitude.toString()
                val lon = location.longitude.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    getLocationWeatherDetails(lat, lon)
                }
            }
        }
    }
}
