package com.example.todoapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.databinding.TodoListItemBinding
import com.example.todoapp.utils.resolveColorAttribute
import com.google.android.material.checkbox.MaterialCheckBox
import java.text.SimpleDateFormat
import java.util.Locale


class TodoItemsAdapter(
    val onItemClicked: (TodoItem) -> Unit,
    val onCheckboxToggle: (TodoItem) -> Unit
) : ListAdapter<TodoItem, TodoItemsAdapter.TodoItemViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TodoItem>() {
            override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
                return oldItem == newItem
            }
        }
    }


    class TodoItemViewHolder(
        private var binding: TodoListItemBinding,
        val onCheckboxToggle: (TodoItem) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todoItem: TodoItem) {
            binding.apply {

                when (todoItem.priority) {
                    Priority.LOW -> {
                        priorityImageView.setImageDrawable(
                            ContextCompat.getDrawable(
                                binding.root.context,
                                R.drawable.ic_priority_low
                            )
                        )
                    }

                    Priority.HIGH -> {
                        priorityImageView.setImageDrawable(
                            ContextCompat.getDrawable(
                                binding.root.context,
                                R.drawable.ic_priority_high
                            )
                        )
                        materialCheckBox.isErrorShown = true
                        materialCheckBox.addOnCheckedStateChangedListener { checkBox, state ->
                            checkBox.isErrorShown = MaterialCheckBox.STATE_CHECKED != state
                        }
                    }

                    else -> {}
                }

                materialCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        bodyTextView.setTextColor(
                            binding.root.context.resolveColorAttribute(R.attr.label_tertiary)
                        )
                    } else {
                        bodyTextView.setTextColor(
                            binding.root.context.resolveColorAttribute(R.attr.label_primary)
                        )
                    }
                    bodyTextView.paint.isStrikeThruText = isChecked
                    // TODO set callback
                    //onCheckboxToggle(todoItem)
                }
                materialCheckBox.isChecked = todoItem.isDone
                bodyTextView.text = todoItem.text


                if (todoItem.dateDeadline == null) {
                    dateDeadlineTextView.visibility = View.GONE
                } else {
                    val pattern = "dd MMM yyyy"
                    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
                    val dateText = todoItem.dateDeadline?.let { formatter.format(it) }
                    dateDeadlineTextView.text = dateText
                }

            }
        }
    }

    override fun onCurrentListChanged(
        previousList: MutableList<TodoItem>,
        currentList: MutableList<TodoItem>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        Log.d("DS", "onCurrentListChanged: changed")
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodoItemViewHolder {
        val viewHolder = TodoItemViewHolder(
            TodoListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onCheckboxToggle
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
