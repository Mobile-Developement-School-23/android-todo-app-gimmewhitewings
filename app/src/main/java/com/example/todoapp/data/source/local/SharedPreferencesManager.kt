package com.example.todoapp.data.source.local

import android.content.SharedPreferences
import com.example.todoapp.di.scope.AppScope
import java.util.UUID
import javax.inject.Inject

/**
 * Shared preferences manager - helper class for shared preferences
 *
 * @property sharedPreferences
 * @constructor Create empty Shared preferences manager
 */
@AppScope
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
