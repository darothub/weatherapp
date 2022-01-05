package com.darothub.weatherapp.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.darothub.weatherapp.R
import com.darothub.weatherapp.databinding.ActivityMainBinding
import com.darotpeacedude.eivom.utils.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
const val ARG_OBJECT = "object"
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    lateinit var adapter: ViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        adapter = ViewPagerAdapter(this, 3) { position ->
            TabFragment.newInstance(position + 1)
        }
        binding.vp.adapter = adapter
        TabLayoutMediator(binding.mainTabLayout, binding.vp) { tab, position ->
            when (position) {
                0 -> tab.text = "Today"
                1 -> tab.text = "Tomorrow"
                2 -> tab.text = "Later"
            }
        }.attach()

//        val s = SpannableString("25oC")
//        s.setSpan(SuperscriptSpan(), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        findViewById<TextView>(R.id.temp_tv).text = s
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
