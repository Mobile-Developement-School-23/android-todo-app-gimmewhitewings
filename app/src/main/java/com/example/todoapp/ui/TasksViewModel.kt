package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TasksUiState(
    val todoItemsList: List<TodoItemUiState> = emptyList(),
    val completedTodoItemsNumber: Int = 0,
    val showUncompletedItems: Boolean = false,
    val showError: Boolean = false
)

class TasksViewModel @AssistedInject constructor(
    private val repository: TodoItemsRepository
) : ViewModel() {

    @AssistedFactory
    interface TasksViewModelFactory {
        fun create(): TasksViewModel
    }

    class Factory(private val factory: TasksViewModelFactory) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return factory.create() as T
        }
    }

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    private lateinit var allTodoItems: List<TodoItem>
    private lateinit var uncompletedTodoItems: List<TodoItem>

    init {
        viewModelScope.launch {
            repository.errorFlow.collect { result ->
                _uiState.update {
                    it.copy(
                        showError = result.isFailure
                    )
                }
            }
        }
        updateRepo()
        fetchTodoItems()
    }

    fun updateRepo() {
        viewModelScope.launch {
            repository.update()
        }
    }

    private fun fetchTodoItems() {
        viewModelScope.launch {
            repository.todoItems.collect { list ->
                allTodoItems = list
                uncompletedTodoItems = list.filter { !it.isCompleted }
                updateTodoItemsList()
            }
        }
    }

    private fun updateTodoItemsList() {
        _uiState.update {
            it.copy(
                todoItemsList = (if (it.showUncompletedItems) allTodoItems else uncompletedTodoItems)
                    .map { item -> item.convertToUiState() },
                completedTodoItemsNumber = allTodoItems.size - uncompletedTodoItems.size
            )
        }
    }

    fun showUncompletedTodoItems(toShow: Boolean) {
        _uiState.update {
            it.copy(
                showUncompletedItems = toShow
            )
        }
        updateTodoItemsList()
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

    fun toggleTodoItemCompletionById(todoItemId: String) {
        viewModelScope.launch {
            repository.toggleTodoItemCompletionById(todoItemId)
        }
    }

//    companion object {
//
//        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            @Suppress("UNCHECKED_CAST")
//            override fun <T : ViewModel> create(
//                modelClass: Class<T>,
//                extras: CreationExtras
//            ): T {
//                // Get the Application object from extras
//                val application = checkNotNull(extras[APPLICATION_KEY])
//                // Create a SavedStateHandle for this ViewModel from extras
//
//                return TasksViewModel(
//                    (application as ToDoApplication).appComponent,
//                ) as T
//            }
//        }
//    }
}
