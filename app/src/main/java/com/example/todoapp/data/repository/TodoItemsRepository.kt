package com.example.todoapp.data.repository

import com.example.todoapp.data.SharedPreferencesManager
import com.example.todoapp.data.local.dao.TodoItemsDao
import com.example.todoapp.data.local.entity.TodoItemEntity
import com.example.todoapp.data.local.entity.asExternalModel
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.model.asNetworkModel
import com.example.todoapp.data.remote.ApiItemMessage
import com.example.todoapp.data.remote.TodoApiService
import com.example.todoapp.data.remote.asEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TodoItemsRepository(
    private val todoItemsDao: TodoItemsDao,
    private val todoApiService: TodoApiService,
    private val sharedPreferencesManager: SharedPreferencesManager
) {

    private val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var revision: Int? = null

    val todoItems: Flow<List<TodoItem>> = todoItemsDao.getAllTodoItemsStream().map {
        it.map(TodoItemEntity::asExternalModel)
    }

//    init {
//        update()
//    }

    fun update() {
        ioScope.launch {
            try {
                val response = todoApiService.getTodoItems()
                revision = response.body()?.revision
                val remoteTodoItems =
                    response.body()?.todoItemNetworkModelList?.map { it.asEntity() }
                val localTodoItems = todoItemsDao.getAllTodoItemsSnapshot()

                remoteTodoItems?.let {
                    todoItemsDao.upsertTodoItems(it)
                    val todoItemsToDelete = localTodoItems.minus(remoteTodoItems)
                    todoItemsDao.deleteTodoItems(todoItemsToDelete)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    suspend fun updateTodoItem(todoItem: TodoItem) {
        todoApiService.editTodoItem(
            revision!!,
            todoItem.id,
            ApiItemMessage(
                todoItemNetworkModel = todoItem.asNetworkModel()
                    .copy(lastUpdatedBy = sharedPreferencesManager.getDeviceId())
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
                todoItemNetworkModel = todoItem.asNetworkModel().copy(
                    lastUpdatedBy = sharedPreferencesManager.getDeviceId()
                ),
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