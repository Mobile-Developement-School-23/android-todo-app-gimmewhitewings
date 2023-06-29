package com.example.todoapp

import android.app.Application
import com.example.todoapp.data.local.db.TodoItemsDatabase
import com.example.todoapp.data.remote.TodoApi
import com.example.todoapp.data.repository.TodoItemsRepository

class ToDoApplication : Application() {
    private val db by lazy { TodoItemsDatabase.getDatabase(this) }

    val repository: TodoItemsRepository by lazy {
        TodoItemsRepository(
            db.todoItemsDao(),
            TodoApi.retrofitService
        )
    }
}