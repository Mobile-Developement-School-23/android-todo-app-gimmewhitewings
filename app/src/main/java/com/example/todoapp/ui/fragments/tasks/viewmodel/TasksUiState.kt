package com.example.todoapp.ui.fragments.tasks.viewmodel

data class TasksUiState(
    val todoItemsList: List<TodoItemUiState> = emptyList(),
    val completedTodoItemsNumber: Int = 0,
    val showUncompletedItems: Boolean = false,
    val showError: Boolean = false
)
