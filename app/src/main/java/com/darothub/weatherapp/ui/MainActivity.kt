package com.darothub.weatherapp.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.SuperscriptSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.darothub.weatherapp.R
import com.darothub.weatherapp.databinding.ActivityMainBinding
import com.darotpeacedude.eivom.utils.viewBinding

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val s = SpannableString("25oC")
        s.setSpan(SuperscriptSpan(), 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        findViewById<TextView>(R.id.temp_tv).text = s
    }
}
