package com.example.todoapp.data.repository

import android.util.Log
import com.example.todoapp.data.SharedPreferencesManager
import com.example.todoapp.data.local.dao.TodoItemsDao
import com.example.todoapp.data.local.entity.TodoItemEntity
import com.example.todoapp.data.local.entity.toDomain
import com.example.todoapp.data.local.entity.toDto
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.model.toDto
import com.example.todoapp.data.model.toEntity
import com.example.todoapp.data.remote.TodoApiService
import com.example.todoapp.data.remote.models.ApiItemMessage
import com.example.todoapp.data.remote.models.ApiListMessage
import com.example.todoapp.data.remote.models.asEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoItemsRepository @Inject constructor(
    private val todoItemsDao: TodoItemsDao,
    private val todoApiService: TodoApiService,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val externalScope: CoroutineScope
) {
    @Volatile
    private var revision: Int? = null

    private val _errorFlow = MutableStateFlow<Result<Any>>(Result.success(true))
    val errorFlow = _errorFlow.asStateFlow()

    val todoItems: Flow<List<TodoItem>> = todoItemsDao.getAllTodoItemsStream().map {
        it.map(TodoItemEntity::toDomain)
    }

    suspend fun update() {
        Log.d("update", "update: called")
        _errorFlow.value = externalScope.runCatching {
            val response = todoApiService.getTodoItems()
            Log.d("update", "update: ${response.isSuccessful}")
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
            true
        }
    }


    suspend fun updateTodoItem(todoItem: TodoItem) {
        _errorFlow.value = externalScope.runCatching {
            todoItemsDao.updateTodoItem(todoItem.toEntity())
            todoApiService.editTodoItem(
                revision!!,
                todoItem.id,
                ApiItemMessage(
                    todoItemNetworkModel = todoItem.toDto()
                        .copy(lastUpdatedBy = sharedPreferencesManager.getDeviceId())
                )
            )
        }
    }

    suspend fun patchTodoItems() {
        externalScope.launch {
            val todoItems = todoItemsDao.getAllTodoItemsSnapshot()
            todoApiService.updateList(
                0,
                ApiListMessage(
                    todoItemNetworkModelList = todoItems.map {
                        it.toDto().copy(
                            lastUpdatedBy = sharedPreferencesManager.getDeviceId()
                        )
                    }
                ))
        }
    }

    suspend fun getTodoItemById(itemId: String): TodoItem? {
        return todoItemsDao.getTodoItemById(itemId)?.toDomain()
    }

    suspend fun deleteTodoItemById(itemId: String) {
        _errorFlow.value = externalScope.runCatching {
            todoItemsDao.deleteTodoItemById(itemId)
            todoApiService.deleteTodoItem(revision!!, itemId)
        }
    }

    suspend fun addTodoItem(todoItem: TodoItem) {
        _errorFlow.value = externalScope.runCatching {
            todoItemsDao.addTodoItem(todoItem.toEntity())
            todoApiService.addTodoItem(
                revision!!,
                ApiItemMessage(
                    todoItemNetworkModel = todoItem.toDto().copy(
                        lastUpdatedBy = sharedPreferencesManager.getDeviceId()
                    ),
                )
            )
        }
    }

    suspend fun toggleTodoItemCompletionById(todoItemId: String) {
        _errorFlow.value = externalScope.runCatching {
            todoItemsDao.toggleTodoItemCompletionById(todoItemId)
            val updatedItem = todoItemsDao.getTodoItemById(todoItemId)
            todoApiService.editTodoItem(
                revision!!,
                todoItemId,
                ApiItemMessage(
                    todoItemNetworkModel = updatedItem?.toDto()
                )
            )
            true
        }
    }
}
