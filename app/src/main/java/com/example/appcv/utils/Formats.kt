package com.example.appcv.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class Formats {
    @SuppressLint("WeekBasedYear")
    val format = SimpleDateFormat("dd MMM, YYYY", Locale.ENGLISH)
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
}