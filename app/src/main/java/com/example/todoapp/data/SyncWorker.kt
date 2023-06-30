package com.example.todoapp.data

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoapp.data.repository.TodoItemsRepository


// WorkManager worker class for syncing data
//class SyncWorker(
//    context: Context,
//    workerParams: WorkerParameters,
//    private val repository: TodoItemsRepository
//) : Worker(context, workerParams) {
//    override fun doWork(): Result {
//        return try {
//            repository.syncLocalListOfTodo()
//            Result.success()
//        } catch (e: Exception) {
//            Result.retry()
//        }
//    }
//}