package com.example.todoapp.data.source

import com.example.todoapp.data.models.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemsDataSource {
    fun getAllTodoItems(): Flow<List<TodoItem>>
    suspend fun addTodoItem(newTodoItem: TodoItem)

    suspend fun getTodoItemById(itemId: String): TodoItem?

    suspend fun deleteTodoItemById(itemId: String)
    suspend fun updateTodoItem(todoItem: TodoItem)
    suspend fun toggleStatus(todoItem: TodoItem)
    suspend fun removeTodoItem(todoItem: TodoItem)
}