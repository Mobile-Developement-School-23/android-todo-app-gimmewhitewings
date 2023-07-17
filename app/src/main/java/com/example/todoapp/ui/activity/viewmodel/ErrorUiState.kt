package com.example.todoapp.ui.activity.viewmodel

data class ErrorUiState(
    val isErrorShown: Boolean = false,
    val error: Throwable? = null
)
