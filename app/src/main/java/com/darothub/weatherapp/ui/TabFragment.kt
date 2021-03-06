package com.darothub.weatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.darothub.weatherapp.MainApplication
import com.darothub.weatherapp.R
import com.darothub.weatherapp.databinding.FragmentTabBinding
import com.darothub.weatherapp.model.Hour
import com.darotpeacedude.eivom.utils.viewBinding

/**
 * A simple [Fragment] subclass.
 * Use the [TabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TabFragment : Fragment(R.layout.fragment_tab) {
    private val binding by viewBinding(FragmentTabBinding::bind)
    val viewModel by viewModels<WeatherViewModel> { WeatherViewModelFactory(MainApplication.getRepository()) }
    private lateinit var recyclerViewAdapter: DataAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            val list = getSerializable(ARG_OBJECT) as List<Hour>
            recyclerViewAdapter = DataAdapter()
            recyclerViewAdapter.setData(list)
            binding.rcv.adapter = recyclerViewAdapter
            binding.rcv.layoutManager = LinearLayoutManager(requireContext())
            binding.rcv.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
//            viewModel.getClimateForecast(data.lat, data.lon, data.dt, data.exclude, data.appid)
//
//            viewModel.weatherLiveData.observe(viewLifecycleOwner) { wrState ->
//                when (wrState) {
//                    is UIState.Success<*> -> {
//                        val wr = wrState.data as WeatherResponse
//
//
//                    }
//                }
//            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: List<Hour>) =
            TabFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_OBJECT, param1 as ArrayList<Hour>)
                }
            }
    }
}
