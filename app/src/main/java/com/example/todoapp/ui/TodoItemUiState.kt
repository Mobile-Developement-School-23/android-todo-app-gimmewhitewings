package com.example.todoapp.ui

import com.example.todoapp.data.models.Importance
import java.util.Date


data class TodoItemUiState(
    val id: String,
    val text: String,
    val isCompleted: Boolean = false,
    val deadline: Date? = null,
    val importance: Importance = Importance.COMMON
)
