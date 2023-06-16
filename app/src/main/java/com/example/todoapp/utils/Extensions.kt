package com.example.todoapp.utils

import android.content.Context
import android.util.TypedValue

fun Context.resolveColorAttribute(attributeResId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attributeResId, typedValue, true)
    return typedValue.data
}