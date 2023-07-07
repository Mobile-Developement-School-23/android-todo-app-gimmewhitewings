package com.example.todoapp.ui.adapters

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.model.Importance
import com.example.todoapp.databinding.TodoListItemBinding
import com.example.todoapp.ui.TasksViewModel
import com.example.todoapp.ui.TodoItemUiState
import com.example.todoapp.utils.DateFormatter
import com.example.todoapp.utils.resolveColorAttribute

class TodoItemViewHolder(
    private val binding: TodoListItemBinding,
    private val viewModel: TasksViewModel
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(todoItemUiState: TodoItemUiState) {
        bindBodyTextView(todoItemUiState)
        bindImportanceIcon(todoItemUiState)
        bindMaterialCheckbox(todoItemUiState)
        bindDeadlineText(todoItemUiState)
    }

    private fun bindMaterialCheckbox(todoItemUiState: TodoItemUiState) {
        binding.materialCheckBox.apply {
            isChecked = todoItemUiState.isCompleted
            isErrorShown = todoItemUiState.importance == Importance.HIGH
            setOnClickListener {
                viewModel.toggleTodoItemCompletionById(todoItemUiState.id)
            }
        }
    }

    private fun bindBodyTextView(todoItemUiState: TodoItemUiState) {
        binding.bodyTextView.apply {
            text = todoItemUiState.text
            paint.isStrikeThruText = todoItemUiState.isCompleted
            setTextColor(
                itemView.rootView.context.resolveColorAttribute(
                    if (todoItemUiState.isCompleted) R.attr.label_tertiary else R.attr.label_primary
                )
            )
        }
    }

    private fun bindDeadlineText(todoItemUiState: TodoItemUiState) {
        val dateText = todoItemUiState.deadline?.let { DateFormatter.formatter.format(it) }
        binding.dateDeadlineTextView.apply {
            isVisible = todoItemUiState.deadline != null
            text = dateText ?: ""
        }
    }

    private fun bindImportanceIcon(todoItemUiState: TodoItemUiState) {
        val importance = todoItemUiState.importance
        binding.importanceImageView.apply {
            isVisible =
                importance == Importance.LOW || importance == Importance.HIGH
            when (importance) {
                Importance.HIGH -> setImageResource(R.drawable.ic_priority_high)
                Importance.COMMON -> {
                    visibility = View.INVISIBLE
                }

                Importance.LOW -> setImageResource(R.drawable.ic_priority_low)
            }
        }
    }
}