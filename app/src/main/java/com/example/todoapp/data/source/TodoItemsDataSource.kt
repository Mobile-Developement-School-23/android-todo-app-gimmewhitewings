package com.example.todoapp.data.source

import com.example.todoapp.data.models.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemsDataSource {
    fun getAllTodoItems(): Flow<List<TodoItem>>
    suspend fun addTodoItem(todoItem: TodoItem)
}