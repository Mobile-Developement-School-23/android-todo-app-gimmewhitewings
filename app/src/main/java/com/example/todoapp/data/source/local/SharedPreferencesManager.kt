package com.example.todoapp.data.source.local

import android.content.SharedPreferences
import com.example.todoapp.di.scope.AppScope
import com.example.todoapp.ui.theme.ApplicationTheme
import com.example.todoapp.ui.theme.toApplicationTheme
import com.example.todoapp.utils.APPLICATION_THEME_KEY
import com.example.todoapp.utils.DEVICE_ID_KEY
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
        val deviceId = sharedPreferences.getString(DEVICE_ID_KEY, null)
        if (deviceId == null) {
            val newDeviceId = UUID.randomUUID().toString()
            sharedPreferences.edit().putString(DEVICE_ID_KEY, newDeviceId).apply()
            return newDeviceId
        }
        return deviceId
    }

    fun getApplicationTheme(): ApplicationTheme {
        val appTheme = sharedPreferences.getInt(APPLICATION_THEME_KEY, -1)
        if (appTheme == -1) {
            val defaultTheme = ApplicationTheme.SYSTEM
            sharedPreferences.edit().putInt(APPLICATION_THEME_KEY, defaultTheme.ordinal).apply()
            return defaultTheme
        }
        return appTheme.toApplicationTheme()
    }

    fun setApplicationTheme(appTheme: ApplicationTheme) {
        sharedPreferences.edit().putInt(APPLICATION_THEME_KEY, appTheme.ordinal).apply()
    }
}
