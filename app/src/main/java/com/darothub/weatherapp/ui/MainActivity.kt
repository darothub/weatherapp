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
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.araujo.jordan.excuseme.ExcuseMe
import com.darothub.weatherapp.MainApplication
import com.darothub.weatherapp.R
import com.darothub.weatherapp.databinding.ActivityMainBinding
import com.darothub.weatherapp.helper.convertLongToTime
import com.darothub.weatherapp.helper.setTextsColorToWhite
import com.darothub.weatherapp.model.QueryRequest
import com.darothub.weatherapp.model.Temp
import com.darothub.weatherapp.model.UIState
import com.darothub.weatherapp.model.WeatherResponse
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
            getLocationWeatherDetails(lat, lng)
        }
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun getLocationWeatherDetails(
        lat: String,
        lng: String
    ) {
        viewModel.getClimates(lat, lng, "minutely", API_KEY)
        viewModel.weatherLiveData.observe(this@MainActivity) { wrState ->
            when (wrState) {
                is UIState.Success<*> -> {
                    binding.main.root.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    val wr = wrState.data as WeatherResponse
                    var tempInCelsius = when (wr.current.temp) {
                        is Double -> wr.current.temp
                        is Temp -> wr.current.temp.min
                        else -> 0.0
                    }
                    var str = "$tempInCelsius" + "oC"
                    val indexOfO = str.indexOf('o')
                    val s = SpannableString(str)
                    s.setSpan(
                        SuperscriptSpan(),
                        indexOfO,
                        str.length - 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
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
                        val desc = wr.current.weather[0].description
                        root.setBackgroundColor(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.purple_500
                            )
                        )
                        temp.text = s
                        description.text = desc
                        wind.text = "Wind: ${wr.current.windSpeed}m/s"
                        pressure.text = "Pressure: ${wr.current.pressure}hPa"
                        humidity.text = "Humidity: ${wr.current.humidity}%"
                        sunrise.text =
                            "Sunrise: ${wr.current.sunrise?.let { convertLongToTime(it) }}"
                        sunset.text = "Sunset: ${wr.current.sunset?.let { convertLongToTime(it) }}"
                        updateDateTv.text = "Last update: ${convertLongToTime(Date().time)}"
                        when {
                            desc.contains("rain", true) -> binding.main.weatherImage.setImageDrawable(
                                ContextCompat.getDrawable(this@MainActivity, R.drawable.rain)
                            )
                            desc.contains("sun", true) -> binding.main.weatherImage.setImageDrawable(
                                ContextCompat.getDrawable(this@MainActivity, R.drawable.sunny)
                            )
                            desc.contains("sun", true) && desc.contains("cloud", true) -> binding.main.weatherImage.setImageDrawable(
                                ContextCompat.getDrawable(this@MainActivity, R.drawable.suncloud)
                            )
                        }
                    }

                    adapter = ViewPagerAdapter(this@MainActivity, 3) { position ->
                        var queryRequest: QueryRequest? = null
                        when (position) {
                            0 -> {
                                queryRequest = QueryRequest(
                                    lat,
                                    lng,
                                    wr.daily[1].dt.toString(),
                                    "minutely",
                                    API_KEY
                                )
                                TabFragment.newInstance(queryRequest)
                            }
                            1 -> {
                                queryRequest = QueryRequest(
                                    lat,
                                    lng,
                                    wr.daily[2].dt.toString(),
                                    "minutely",
                                    API_KEY
                                )
                                TabFragment.newInstance(queryRequest)
                            }
                            else -> {
                                queryRequest = QueryRequest(
                                    lat,
                                    lng,
                                    wr.daily[3].dt.toString(),
                                    "minutely",
                                    API_KEY
                                )
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
                }
                is UIState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.main.root.visibility = View.GONE
                }
            }
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
                getLocationWeatherDetails(lat, lon)
            }
        }
    }
}
