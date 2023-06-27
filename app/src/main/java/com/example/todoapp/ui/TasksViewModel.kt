package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todoapp.ToDoApplication
import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TasksUiState(
    val todoItemsList: List<TodoItemUiState> = emptyList(),
    val completedTodoItemsNumber: Int = 0
)

class TasksViewModel(
    private val repository: TodoItemsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    init {
        fetchTodoItems()
    }

    fun fetchTodoItems() {
        viewModelScope.launch {
            repository.todoItems.collect { list ->
                _uiState.update {
                    it.copy(
                        todoItemsList = list.filter { !it.isCompleted }
                            .map { item -> item.convertToUiState() },
                        completedTodoItemsNumber = list.count { item -> item.isCompleted }
                    )
                }
            }
        }
    }


    private fun TodoItem.convertToUiState(): TodoItemUiState {
        return TodoItemUiState(
            id = this.id,
            text = this.text,
            isCompleted = this.isCompleted,
            deadline = this.deadline,
            importance = this.importance
        )
    }

    fun deleteTodoItemById(itemId: String) {
        viewModelScope.launch {
            repository.deleteTodoItemById(itemId)
        }
    }

    fun toggleTodoItemCompletionById(todoItemId: String) {
        viewModelScope.launch {
            repository.toggleTodoItemCompletionById(todoItemId)
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras

                return TasksViewModel(
                    (application as ToDoApplication).repository,
                ) as T
            }
        }
    }
}