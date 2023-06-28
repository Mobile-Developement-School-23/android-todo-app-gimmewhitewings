package com.example.todoapp.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.ui.TodoItemUiState

class TodoItemDiffCalculator : DiffUtil.ItemCallback<TodoItemUiState>() {
    override fun areItemsTheSame(
        oldItem: TodoItemUiState,
        newItem: TodoItemUiState
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: TodoItemUiState,
        newItem: TodoItemUiState
    ): Boolean {
        return oldItem == newItem
    }

}