package com.example.todoapp.di.module

import android.app.AlarmManager
import android.content.Context
import android.content.SharedPreferences
import com.example.todoapp.utils.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


@Module
class AppModule(private val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideAlarmManager(context: Context): AlarmManager {
        return context.getSystemService(AlarmManager::class.java)
    }

    @Provides
    fun provideApplicationCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }
}
