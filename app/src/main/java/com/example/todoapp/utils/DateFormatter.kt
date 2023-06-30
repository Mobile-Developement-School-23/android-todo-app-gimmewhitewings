package com.example.todoapp.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {
    val formatter: SimpleDateFormat by lazy { SimpleDateFormat(DATE_PATTERN, Locale.getDefault()) }
}