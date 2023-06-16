package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.data.source.TodoItemsHardcodeDataSource
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {
    private val repository = TodoItemsRepository(TodoItemsHardcodeDataSource())

    var visibleTodoItems = repository.getAllTodoItems()

    fun addNewTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.addTodoItem(todoItem)
        }
    }

    fun toggleIsDone(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.toggleIsDone(todoItem)
        }
    }
}