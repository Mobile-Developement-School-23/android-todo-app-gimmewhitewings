package com.example.todoapp.data.repository

import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.data.source.TodoItemsFakeDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// For the future, when we'll have to implement both local and remote data sources
class TodoItemsRepository(
    private val todoItemsFakeDataSource: TodoItemsFakeDataSource
) {
    val todoItems: Flow<List<TodoItem>> = todoItemsFakeDataSource.todoItems
    val uncompletedTodoItems: Flow<List<TodoItem>> = todoItemsFakeDataSource.todoItems
        .map { list ->
            list.filter { todoItem ->
                !todoItem.isCompleted
            }
        }

    suspend fun getUncompletedTodoItems(): Flow<List<TodoItem>> =
        todoItemsFakeDataSource.getUncompletedTodoItems()

    suspend fun updateTodoItem(todoItem: TodoItem) {
        todoItemsFakeDataSource.updateTodoItem(todoItem)
    }

    suspend fun updateOrAddTodoItem(todoItem: TodoItem) {
        todoItemsFakeDataSource.updateOrAddTodoItem(todoItem)
    }

    suspend fun getTodoItemById(itemId: String): TodoItem? {
        return todoItemsFakeDataSource.getTodoItemById(itemId)
    }

    suspend fun deleteTodoItemById(itemId: String) {
        todoItemsFakeDataSource.deleteTodoItemById(itemId)
    }

    suspend fun addTodoItem(todoItem: TodoItem) {
        todoItemsFakeDataSource.addTodoItem(todoItem)
    }

    suspend fun toggleTodoItemCompletionById(todoItemId: String) {
        todoItemsFakeDataSource.toggleTodoItemCompletionById(todoItemId)
    }
}