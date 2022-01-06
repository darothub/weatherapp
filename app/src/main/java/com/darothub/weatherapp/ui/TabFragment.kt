package com.darothub.weatherapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.darothub.weatherapp.R
import com.darothub.weatherapp.databinding.FragmentTabBinding
import com.darotpeacedude.eivom.utils.viewBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TabFragment : Fragment(R.layout.fragment_tab) {
    private val binding by viewBinding(FragmentTabBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
//            binding.tv.text = getInt(ARG_OBJECT).toString()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TabFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int) =
            TabFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_OBJECT, param1)
                }
            }
    }
}
