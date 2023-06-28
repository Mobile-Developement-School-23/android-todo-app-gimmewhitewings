package com.example.todoapp

import android.app.Application
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.data.source.TodoItemsFakeDataSource

class ToDoApplication : Application() {
    val repository: TodoItemsRepository by lazy { TodoItemsRepository(TodoItemsFakeDataSource()) }
}