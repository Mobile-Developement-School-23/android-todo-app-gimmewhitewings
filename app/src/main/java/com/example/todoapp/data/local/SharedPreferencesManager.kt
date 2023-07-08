package com.example.todoapp.data.local

import android.content.SharedPreferences
import java.util.UUID
import javax.inject.Inject

class SharedPreferencesManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun getDeviceId(): String {
        val deviceId = sharedPreferences.getString("device_id", null)
        if (deviceId == null) {
            val newDeviceId = UUID.randomUUID().toString()
            sharedPreferences.edit().putString("device_id", newDeviceId).apply()
            return newDeviceId
        }
        return deviceId
    }

}