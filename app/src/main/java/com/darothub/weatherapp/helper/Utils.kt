package com.darothub.weatherapp.helper

import android.text.SpannableString
import android.text.Spanned
import android.text.style.SuperscriptSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.darothub.weatherapp.R
import java.text.SimpleDateFormat
import java.util.*

// fun convertKelvinToCelsius(temp: Double): Double {
//    return "%.2f".format(temp - 273.15).toDouble()
// }

fun convertLongToTime(longTime: Long): String {
    val dt = Date(longTime)
    val sdf = SimpleDateFormat("hh:mm a", Locale.US)
    return sdf.format(dt)
}
fun convertEpochTimeToStringFormat(ts: Long?): String {
    if (ts == null) return ""
    // Get instance of calendar
    val calendar = Calendar.getInstance(Locale.getDefault())
    // get current date from ts
    calendar.timeInMillis = Date(ts * 1000).time
    // return formatted date
    return android.text.format.DateFormat.format("E dd.MM.yyyy hh:mm", calendar).toString()
}

fun setTextsColorToWhite(textView: TextView) {
    textView.setTextColor(ContextCompat.getColor(textView.context, R.color.white))
}

fun setTextViewsColor(color: Int, vararg textView: TextView) {
    for (tv in textView) {
        tv.setTextColor(ContextCompat.getColor(tv.context, color))
    }
}

fun convertTempToScientificReading(tempInCelsius: Double): SpannableString {
    var str = "$tempInCelsius" + "oC"
    val indexOfO = str.indexOf('o')
    val s = SpannableString(str)
    s.setSpan(
        SuperscriptSpan(),
        indexOfO,
        str.length - 1,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return s
}
