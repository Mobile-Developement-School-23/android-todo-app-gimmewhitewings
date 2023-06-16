package com.example.todoapp.data.repository

import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.data.source.TodoItemsDataSource
import kotlinx.coroutines.flow.Flow

// For the future, when we'll have to implement both local and remote data sources
class TodoItemsRepository(
    private val localDataSource: TodoItemsDataSource
) {
    fun getAllTodoItems(): Flow<List<TodoItem>> = localDataSource.getAllTodoItems()

    suspend fun addTodoItem(todoItem: TodoItem) {
        localDataSource.addTodoItem(todoItem)
    }

    suspend fun toggleIsDone(todoItem: TodoItem) {
        localDataSource.toggleStatus(todoItem)
    }
}