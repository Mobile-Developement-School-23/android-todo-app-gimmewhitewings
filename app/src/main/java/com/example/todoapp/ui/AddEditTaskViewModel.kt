package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

data class AddEditUiState(
    val text: String = "",
    val importance: Importance = Importance.COMMON,
    val deadline: Date? = null,
)

class AddEditTaskViewModel @AssistedInject constructor(
    private val repository: TodoItemsRepository
) : ViewModel() {

    @AssistedFactory
    interface AddEditTasksViewModelFactory {
        fun create(): AddEditTaskViewModel
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val factory: AddEditTasksViewModelFactory) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return factory.create() as T
        }
    }

    private lateinit var editedItem: TodoItem
    private var isItemLoaded = false

    fun loadTodoItem(todoItemId: String) {
        if (!isItemLoaded) {
            viewModelScope.launch {
                editedItem = repository.getTodoItemById(todoItemId)!!
                _uiState.update {
                    it.copy(
                        text = editedItem.text,
                        importance = editedItem.importance,
                        deadline = editedItem.deadline
                    )
                }
                isItemLoaded = true
            }
        }
    }


    private val _uiState = MutableStateFlow(AddEditUiState())
    val uiState: StateFlow<AddEditUiState> = _uiState.asStateFlow()

    fun setImportance(importance: Importance) {
        _uiState.update {
            it.copy(
                importance = importance
            )
        }
    }

    fun setDate(deadline: Date?) {
        _uiState.update {
            it.copy(
                deadline = deadline
            )
        }
    }

    fun saveTodoItem(todoItemId: String?) {
        if (todoItemId == null) {
            addNewTodoItem()
        } else {
            updateTodoItem()
        }
    }

    private fun addNewTodoItem() {
        viewModelScope.launch {
            val enteredData = _uiState.value
            repository.addTodoItem(
                TodoItem(
                    id = UUID.randomUUID().toString(),
                    isCompleted = false,
                    createdAt = Date(),
                    importance = enteredData.importance,
                    text = enteredData.text,
                    deadline = enteredData.deadline
                )
            )
        }
    }

    private fun updateTodoItem() {
        viewModelScope.launch {
            val enteredData = _uiState.value
            repository.updateTodoItem(
                TodoItem(
                    id = editedItem.id,
                    text = enteredData.text,
                    importance = enteredData.importance,
                    deadline = enteredData.deadline,
                    modifiedAt = Date(),
                    createdAt = editedItem.createdAt,
                    isCompleted = editedItem.isCompleted
                )
            )
        }
    }

    fun deleteTodoItem(todoItemId: String) {
        viewModelScope.launch {
            repository.deleteTodoItemById(todoItemId)
        }
    }

    fun setText(text: String) {
        _uiState.update {
            it.copy(
                text = text
            )
        }
    }
}
