package com.example.todoapp.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.data.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DownloadWorker @Inject constructor(
    context: Context,
    workerParams: WorkerParameters,
    private val repository: TodoItemsRepository
) :
    CoroutineWorker(
        context,
        workerParams
    ) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            repository.update()
            return@withContext Result.success()
        } catch (e: Exception) {
            return@withContext Result.retry()
        }
    }
}
