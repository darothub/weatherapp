package com.darothub.weatherapp.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.SuperscriptSpan
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.darothub.weatherapp.MainApplication
import com.darothub.weatherapp.R
import com.darothub.weatherapp.databinding.FragmentMainBinding
import com.darothub.weatherapp.helper.convertKelvinToCelsius
import com.darothub.weatherapp.model.QueryRequest
import com.darothub.weatherapp.model.Temp
import com.darotpeacedude.eivom.utils.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)
    val viewModel by activityViewModels<WeatherViewModel> { WeatherViewModelFactory(MainApplication.getRepository()) }
    private lateinit var recyclerViewAdapter: DataAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            val data = getSerializable(ARG_OBJECT) as QueryRequest
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getClimateForecast(data.lat, data.lon, data.dt, data.exclude, data.appid)
                Log.d("MainFrag", data.dt.toString())
                withContext(Dispatchers.Main) {
                    viewModel.weatherLiveData.observe(
                        viewLifecycleOwner,
                        { wr ->
                            Log.d("TabWR", wr.toString())
                            var tempInCelsius = when (wr.current.temp) {
                                is Double -> convertKelvinToCelsius(wr.current.temp)
                                is Temp -> convertKelvinToCelsius(wr.current.temp.min)
                                else -> 0.0
                            }

                            val s = SpannableString("$tempInCelsius" + "oC")
                            s.setSpan(SuperscriptSpan(), 5, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            recyclerViewAdapter = DataAdapter()
                            recyclerViewAdapter.setData(wr.hourly)
                            binding.rcv.adapter = recyclerViewAdapter
                            binding.rcv.layoutManager = LinearLayoutManager(requireContext())
                        }
                    )
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: QueryRequest?) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_OBJECT, param1)
                }
            }
    }
}
