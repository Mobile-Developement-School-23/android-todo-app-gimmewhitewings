package com.example.todoapp.data.repository

import com.example.todoapp.data.local.dao.TodoItemsDao
import com.example.todoapp.data.local.entity.TodoItemEntity
import com.example.todoapp.data.local.entity.asExternalModel
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.model.asEntity
import com.example.todoapp.data.remote.TodoApiService
import com.example.todoapp.data.remote.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoItemsRepository(
    private val todoItemsDao: TodoItemsDao,
    private val todoApiService: TodoApiService
) {
    suspend fun update() {
        val response = todoApiService.getTodoItems()
        if (response.isSuccessful) {
            response.body()?.todoItemNetworkModelList?.forEach {
                todoItemsDao.addTodoItem(it.asEntity())
            }
        }
    }


    val todoItems: Flow<List<TodoItem>> = todoItemsDao.getAllTodoItems().map {
        it.map(TodoItemEntity::asExternalModel)
    }

    suspend fun updateTodoItem(todoItem: TodoItem) {
        todoItemsDao.updateTodoItem(todoItem.asEntity())
    }

    suspend fun getTodoItemById(itemId: String): TodoItem? {
        return todoItemsDao.getTodoItemById(itemId)?.asExternalModel()
    }

    suspend fun deleteTodoItemById(itemId: String) {
        todoItemsDao.deleteTodoItemById(itemId)
    }

    suspend fun addTodoItem(todoItem: TodoItem) {
        todoItemsDao.addTodoItem(todoItem.asEntity())
    }

    suspend fun toggleTodoItemCompletionById(todoItemId: String) {
        todoItemsDao.toggleTodoItemCompletionById(todoItemId)
    }
}