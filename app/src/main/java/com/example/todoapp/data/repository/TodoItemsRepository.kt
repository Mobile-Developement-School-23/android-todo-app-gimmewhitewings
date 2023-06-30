package com.example.todoapp.data.repository

import com.example.todoapp.data.local.dao.TodoItemsDao
import com.example.todoapp.data.local.entity.TodoItemEntity
import com.example.todoapp.data.local.entity.asExternalModel
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.model.asNetworkModel
import com.example.todoapp.data.remote.ApiItemMessage
import com.example.todoapp.data.remote.TodoApiService
import com.example.todoapp.data.remote.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoItemsRepository(
    private val todoItemsDao: TodoItemsDao,
    private val todoApiService: TodoApiService
    //private val sharedPreferencesManager: SharedPreferencesManager
) {
    private var revision: Int? = null

    val todoItems: Flow<List<TodoItem>> = todoItemsDao.getAllTodoItems().map {
        it.map(TodoItemEntity::asExternalModel)
    }

    suspend fun update() {
        val response = todoApiService.getTodoItems()
        if (response.isSuccessful) {

            revision = response.body()?.revision
            val networkIds = response.body()?.todoItemNetworkModelList?.map { it.id }
            val dbIds = todoItemsDao.getIds()

            networkIds?.let {
                val needToDelete = dbIds.subtract(it.toSet())
                needToDelete.forEach { id ->
                    todoItemsDao.deleteTodoItemById(id)
                }
            }

            response.body()?.todoItemNetworkModelList?.forEach {
                todoItemsDao.addTodoItem(it.asEntity())
            }
        }
    }


    suspend fun updateTodoItem(todoItem: TodoItem) {
        todoApiService.editTodoItem(
            revision!!,
            todoItem.id,
            ApiItemMessage(
                todoItemNetworkModel = todoItem.asNetworkModel()
            )
        )
        update()
        //todoItemsDao.updateTodoItem(todoItem.asEntity())
    }

    suspend fun getTodoItemById(itemId: String): TodoItem? {
        return todoItemsDao.getTodoItemById(itemId)?.asExternalModel()
    }

    suspend fun deleteTodoItemById(itemId: String) {
        todoApiService.deleteTodoItem(revision!!, itemId)
        update()
        //todoItemsDao.deleteTodoItemById(itemId)
    }

    suspend fun addTodoItem(todoItem: TodoItem) {
        (todoApiService.addTodoItem(
            revision!!,
            ApiItemMessage(
                todoItemNetworkModel = todoItem.asNetworkModel(),
            )
        ))
        update()
        //todoItemsDao.addTodoItem(todoItem.asEntity())
    }

    suspend fun toggleTodoItemCompletionById(todoItemId: String) {
        todoItemsDao.toggleTodoItemCompletionById(todoItemId)
        updateTodoItem(getTodoItemById(todoItemId)!!)
    }
}