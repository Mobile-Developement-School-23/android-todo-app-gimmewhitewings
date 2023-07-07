package com.example.todoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.todoapp.data.repository.TodoItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActivityViewModel(repository: TodoItemsRepository) : ViewModel() {


    private var _errorState = MutableStateFlow(ErrorUiState())
    val errorState = _errorState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.errorFlow.collect { result ->
                _errorState.update {
                    it.copy(
                        isErrorShown = result.isFailure,
                        error = result.exceptionOrNull()
                    )
                }
            }
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
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras

                return ActivityViewModel(
                    (application as ToDoApplication).repository,
                ) as T
            }
        }
    }
}

data class ErrorUiState(
    val isErrorShown: Boolean = false,
    val error: Throwable? = null
)
