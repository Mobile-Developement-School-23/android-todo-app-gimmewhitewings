package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.data.source.TodoItemsHardcodeDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {
    private val repository = TodoItemsRepository(TodoItemsHardcodeDataSource())

    var visibleTodoItems = repository.getAllTodoItems()

    private val _selectedTodoItem: MutableStateFlow<TodoItem?> = MutableStateFlow(null)
    val selectedTodoItem: Flow<TodoItem?> = _selectedTodoItem

    fun addNewTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.addTodoItem(todoItem)
        }
    }

    fun deleteTodoItemById(itemId: String) {
        viewModelScope.launch {
            repository.deleteTodoItemById(itemId)
        }
    }

    fun getTodoItemById(id: String) {
        viewModelScope.launch {
            val todoItem = repository.getTodoItemById(id)
            _selectedTodoItem.value = todoItem
        }
    }

    fun updateTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.updateTodoItem(todoItem)
        }
    }

    fun toggleIsDone(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.toggleIsDone(todoItem)
        }
    }
}