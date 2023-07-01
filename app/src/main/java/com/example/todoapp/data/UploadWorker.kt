package com.example.todoapp.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.ToDoApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UploadWorker(
    context: Context,
    workerParams: WorkerParameters,
//    private val todoApiService: TodoApiService,
//    private val todoItemsDao: TodoItemsDao
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val todoItemsRepository = (applicationContext as ToDoApplication).repository
        try {
            todoItemsRepository.patchTodoItems()
            todoItemsRepository.update()
            return@withContext Result.success()
        } catch (e: Exception) {
            return@withContext Result.retry()
        }
    }
}