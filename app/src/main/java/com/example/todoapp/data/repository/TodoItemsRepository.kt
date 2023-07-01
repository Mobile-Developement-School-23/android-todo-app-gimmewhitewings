package com.example.todoapp.data.repository

import com.example.todoapp.data.SharedPreferencesManager
import com.example.todoapp.data.local.dao.TodoItemsDao
import com.example.todoapp.data.local.entity.TodoItemEntity
import com.example.todoapp.data.local.entity.asExternalModel
import com.example.todoapp.data.local.entity.asNetworkModel
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.model.asEntity
import com.example.todoapp.data.model.asNetworkModel
import com.example.todoapp.data.remote.ApiItemMessage
import com.example.todoapp.data.remote.ApiListMessage
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

    init {
        update()
    }

    fun update() {
        ioScope.launch {
            try {
                val response = todoApiService.getTodoItems()
                revision = response.body()?.revision
                val remoteTodoItems =
                    response.body()?.todoItemNetworkModelList?.map { it.asEntity() }
                val localTodoItems = todoItemsDao.getAllTodoItemsSnapshot()

                remoteTodoItems?.let { list ->
                    todoItemsDao.upsertTodoItems(list)
                    val todoItemsToDelete =
                        localTodoItems.map { it.id }.minus(remoteTodoItems.map { it.id }.toSet())
                    todoItemsToDelete.forEach {
                        todoItemsDao.deleteTodoItemById(it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    suspend fun updateTodoItem(todoItem: TodoItem) {
        try {
            todoApiService.editTodoItem(
                revision!!,
                todoItem.id,
                ApiItemMessage(
                    todoItemNetworkModel = todoItem.asNetworkModel()
                        .copy(lastUpdatedBy = sharedPreferencesManager.getDeviceId())
                )
            )
            update()
        } catch (e: Exception) {
            e.printStackTrace()
            todoItemsDao.updateTodoItem(todoItem.asEntity())
        }
    }

    suspend fun patchTodoItems() {
        try {
            val todoItems = todoItemsDao.getAllTodoItemsSnapshot()
            todoApiService.updateList(
                0,
                ApiListMessage(
                    todoItemNetworkModelList = todoItems.map {
                        it.asNetworkModel().copy(
                            lastUpdatedBy = sharedPreferencesManager.getDeviceId()
                        )
                    }
                ))
            update()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getTodoItemById(itemId: String): TodoItem? {
        return todoItemsDao.getTodoItemById(itemId)?.asExternalModel()
    }

    suspend fun deleteTodoItemById(itemId: String) {
        try {
            todoApiService.deleteTodoItem(revision!!, itemId)
            update()
        } catch (e: Exception) {
            e.printStackTrace()
            todoItemsDao.deleteTodoItemById(itemId)
        }
    }

    suspend fun addTodoItem(todoItem: TodoItem) {
        try {
            todoApiService.addTodoItem(
                revision!!,
                ApiItemMessage(
                    todoItemNetworkModel = todoItem.asNetworkModel().copy(
                        lastUpdatedBy = sharedPreferencesManager.getDeviceId()
                    ),
                )
            )
            update()
        } catch (e: Exception) {
            e.printStackTrace()
            todoItemsDao.addTodoItem(todoItem.asEntity())
        }
    }

    suspend fun toggleTodoItemCompletionById(todoItemId: String) {
        todoItemsDao.toggleTodoItemCompletionById(todoItemId)
        try {
            updateTodoItem(getTodoItemById(todoItemId)!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}