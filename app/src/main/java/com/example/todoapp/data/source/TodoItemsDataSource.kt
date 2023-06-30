package com.example.todoapp.data.source

import com.example.todoapp.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemsDataSource {
    suspend fun getAllTodoItems(): Flow<List<TodoItem>>
    suspend fun addTodoItem(newTodoItem: TodoItem)

    suspend fun getTodoItemById(itemId: String): TodoItem?

    suspend fun deleteTodoItemById(itemId: String)
    suspend fun updateTodoItem(todoItem: TodoItem)
    suspend fun toggleTodoItemCompletionById(todoItemId: String)
    suspend fun removeTodoItem(todoItem: TodoItem)
}