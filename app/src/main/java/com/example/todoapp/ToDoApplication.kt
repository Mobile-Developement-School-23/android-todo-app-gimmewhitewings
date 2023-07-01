package com.example.todoapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todoapp.data.DownloadWorker
import com.example.todoapp.data.SharedPreferencesManager
import com.example.todoapp.data.UploadWorker
import com.example.todoapp.data.local.db.TodoItemsDatabase
import com.example.todoapp.data.remote.AuthInterceptor
import com.example.todoapp.data.remote.TodoApiService
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.utils.AUTH_TOKEN
import com.example.todoapp.utils.BASE_URL
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ToDoApplication : Application() {
    private val moshi = Moshi.Builder()
        .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(AUTH_TOKEN))
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    private val todoApiService: TodoApiService by lazy { retrofit.create(TodoApiService::class.java) }
    private val db by lazy { TodoItemsDatabase.getDatabase(this) }

    fun startUploadWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                1,
                TimeUnit.MINUTES
            )
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            "upload",
            ExistingWorkPolicy.REPLACE,
            uploadWorkRequest
        )
    }

    private fun startDownloadWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val downloadWorkRequest = PeriodicWorkRequestBuilder<DownloadWorker>(
            8, TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                1,
                TimeUnit.MINUTES
            )
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "download",
            ExistingPeriodicWorkPolicy.KEEP,
            downloadWorkRequest
        )
    }

    override fun onCreate() {
        super.onCreate()
        startDownloadWorker()
    }

    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(
            "todo_app_shared_preferences",
            Context.MODE_PRIVATE
        )
    }

    val repository: TodoItemsRepository by lazy {
        TodoItemsRepository(
            db.todoItemsDao(),
            todoApiService,
            SharedPreferencesManager(sharedPreferences)
        )
    }
}
