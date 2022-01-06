package com.darothub.weatherapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.whenCreated
import com.darothub.weatherapp.R
import com.darothub.weatherapp.databinding.FragmentMainBinding
import com.darotpeacedude.eivom.utils.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)
//    val viewModel by activityViewModels<WeatherViewModel> { WeatherViewModelFactory(Dependencies.getRepository()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            lifecycle.whenCreated {
//                viewModel.weatherLiveData.observe(
//                    viewLifecycleOwner,
//                    { wr ->
//                        Log.d("MainFrag", wr.toString())
//                    }
//                )
            }
        }
    }
}
