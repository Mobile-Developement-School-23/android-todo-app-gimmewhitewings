package com.example.todoapp.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormatter {
    val pattern = "dd MMM yyyy"
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
}