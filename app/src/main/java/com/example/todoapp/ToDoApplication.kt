package com.example.todoapp

import android.app.Application
import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todoapp.data.DownloadWorker
import com.example.todoapp.data.UploadWorker
import com.example.todoapp.di.component.AppComponent
import com.example.todoapp.di.component.DaggerAppComponent
import com.example.todoapp.di.module.AppModule
import com.example.todoapp.utils.HOURS_TO_UPDATE
import java.util.concurrent.TimeUnit

class ToDoApplication : Application() {

    lateinit var appComponent: AppComponent

    private val workManager by lazy { WorkManager.getInstance(applicationContext) }

    fun startUploadWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                1,
                TimeUnit.MINUTES
            )
            .build()

        workManager.enqueueUniqueWork(
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
            HOURS_TO_UPDATE, TimeUnit.HOURS
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
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(context = applicationContext))
            .build()
        startDownloadWorker()
    }

}

val Context.appComponent: AppComponent
    get() = when (this) {
        is ToDoApplication -> appComponent
        else -> this.applicationContext.appComponent
    }
