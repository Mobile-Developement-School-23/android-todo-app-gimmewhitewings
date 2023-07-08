package com.example.todoapp.ui.fragments.tasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.todoapp.databinding.TodoListItemBinding
import com.example.todoapp.ui.fragments.tasks.viewmodel.TasksViewModel
import com.example.todoapp.ui.fragments.tasks.viewmodel.TodoItemUiState


class TodoItemsAdapter(
    private val viewModel: TasksViewModel,
    todoItemDiffUtil: TodoItemDiffCalculator,
    private val onItemClicked: (String) -> Unit
) : ListAdapter<TodoItemUiState, TodoItemViewHolder>(todoItemDiffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodoItemViewHolder {
        return TodoItemViewHolder(
            TodoListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            viewModel,
            onItemClicked
        )
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
